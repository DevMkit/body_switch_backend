package kr.co.softhubglobal.service;

import kr.co.softhubglobal.dto.member.MemberDTO;
import kr.co.softhubglobal.dto.member.MemberInfoMapper;
import kr.co.softhubglobal.entity.member.LoginProvider;
import kr.co.softhubglobal.entity.member.Member;
import kr.co.softhubglobal.entity.member.MemberRegisteredType;
import kr.co.softhubglobal.entity.member.MemberStatus;
import kr.co.softhubglobal.entity.user.Role;
import kr.co.softhubglobal.entity.user.User;
import kr.co.softhubglobal.exception.customExceptions.DuplicateResourceException;
import kr.co.softhubglobal.repository.MemberRepository;
import kr.co.softhubglobal.repository.UserRepository;
import kr.co.softhubglobal.utils.RandomCodeGenerator;
import kr.co.softhubglobal.validator.ObjectValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final UserRepository userRepository;
    private final MemberRepository memberRepository;
    private final MemberInfoMapper memberInfoMapper;
    private final ObjectValidator<MemberDTO.MemberCreateRequest> memberCreateRequestObjectValidator;
    private final MessageSource messageSource;
    private final PasswordEncoder passwordEncoder;

    public List<MemberDTO.MemberInfo> getAllMembers() {
        return memberRepository.findAll()
                .stream()
                .map(memberInfoMapper)
                .toList();
    }

    public void createMemberRecord(MemberDTO.MemberCreateRequest memberCreateRequest) {

        memberCreateRequestObjectValidator.validate(memberCreateRequest);

        if(userRepository.existsByUsernameAndRoleIn(memberCreateRequest.getUsername(), List.of(Role.MEMBER, Role.EMPLOYEE))) {
            throw new DuplicateResourceException(
                    messageSource.getMessage("member.username.already.exists", new Object[]{memberCreateRequest.getUsername()}, Locale.ENGLISH));
        }

        memberRepository.save(
                Member.builder()
                        .id(RandomCodeGenerator.generateCode(15))
                        .user(User.builder()
                                .username(memberCreateRequest.getUsername())
                                .name(memberCreateRequest.getName())
                                .phoneNumber(memberCreateRequest.getPhoneNumber())
                                .email(memberCreateRequest.getEmail())
                                .password(passwordEncoder.encode(memberCreateRequest.getPassword()))
                                .role(Role.MEMBER)
                                .build()
                        )
                        .birthDate(memberCreateRequest.getBirthDate())
                        .gender(memberCreateRequest.getGender())
                        .postalCode(memberCreateRequest.getPostalCode())
                        .address(memberCreateRequest.getAddress())
                        .addressDetail(memberCreateRequest.getAddressDetail())
                        .status(MemberStatus.ACTIVE)
                        .loginProvider(LoginProvider.LOCAL)
                        .isSMSReceive(false)
                        .regType(MemberRegisteredType.SELF)
                        .build()
        );
    }
}
