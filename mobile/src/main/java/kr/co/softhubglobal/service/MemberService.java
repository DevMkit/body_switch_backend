package kr.co.softhubglobal.service;

import kr.co.softhubglobal.dto.member.*;
import kr.co.softhubglobal.entity.common.Restrictions;
import kr.co.softhubglobal.entity.member.*;
import kr.co.softhubglobal.entity.user.Role;
import kr.co.softhubglobal.entity.user.User;
import kr.co.softhubglobal.exception.customExceptions.DuplicateResourceException;
import kr.co.softhubglobal.exception.customExceptions.RequestNotAcceptableException;
import kr.co.softhubglobal.exception.customExceptions.ResourceNotFoundException;
import kr.co.softhubglobal.repository.MemberCourseTicketRepository;
import kr.co.softhubglobal.repository.MemberRepository;
import kr.co.softhubglobal.repository.MemberReservationRepository;
import kr.co.softhubglobal.repository.UserRepository;
import kr.co.softhubglobal.utils.FileUploader;
import kr.co.softhubglobal.utils.RandomCodeGenerator;
import kr.co.softhubglobal.validator.ObjectValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final UserRepository userRepository;
    private final MemberRepository memberRepository;
    private final MemberReservationRepository memberReservationRepository;
    private final MemberCourseTicketRepository memberCourseTicketRepository;
    private final MemberInfoMapper memberInfoMapper;
    private final MemberUsageInfoMapper memberUsageInfoMapper;
    private final MemberReservationInfoMapper memberReservationInfoMapper;
    private final MemberReservationDetailInfoMapper memberReservationDetailInfoMapper;
    private final ObjectValidator<MemberDTO.MemberCreateRequest> memberCreateRequestObjectValidator;
    private final ObjectValidator<MemberDTO.MemberUsernameCheckRequest> memberUsernameCheckRequestObjectValidator;
    private final ObjectValidator<MemberDTO.MemberReservationCancelRequest> memberReservationCancelRequestObjectValidator;
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
                        .profileImage(memberCreateRequest.getProfileImage() != null ? FileUploader.uploadFile(memberCreateRequest.getProfileImage()) : null)
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

    public boolean checkUsernameExists(MemberDTO.MemberUsernameCheckRequest memberUsernameCheckRequest) {
        memberUsernameCheckRequestObjectValidator.validate(memberUsernameCheckRequest);
        return userRepository.existsByUsernameAndRoleIn(memberUsernameCheckRequest.getUsername(), List.of(Role.MEMBER, Role.EMPLOYEE));
    }

    public MemberDTO.MemberUsageInfo getMemberUsageInformation(Long userId) {
        return memberRepository.findByUserId(userId)
                .map(memberUsageInfoMapper)
                .orElseThrow(() -> new DuplicateResourceException(
                        messageSource.getMessage("member.user.id.not.found", new Object[]{userId}, Locale.ENGLISH)));
    }

    public List<MemberDTO.MemberReservationInfo> getMemberReservations(Long userId, MemberDTO.MemberReservationSearchRequest memberReservationSearchRequest) {
        Restrictions restrictions = new Restrictions();
        restrictions.eq("member.user.id", userId);
        if(memberReservationSearchRequest.getReservationStatus() != null) {
            restrictions.eq("status", memberReservationSearchRequest.getReservationStatus());
        }
        if(memberReservationSearchRequest.getFromDate() != null && memberReservationSearchRequest.getEndDate() != null) {
            restrictions.between("reservationDate", memberReservationSearchRequest.getFromDate(), memberReservationSearchRequest.getEndDate());
        }
        return memberReservationRepository.findAll(restrictions.output())
                .stream()
                .map(memberReservationInfoMapper)
                .toList();
    }

    public MemberDTO.MemberReservationDetailInfo getMemberReservationDetailInfoById(Long reservationId) {
        return memberReservationRepository.findById(reservationId)
                .map(memberReservationDetailInfoMapper)
                .orElseThrow(() -> new ResourceNotFoundException(
                        messageSource.getMessage("member.reservation.id.not.found", new Object[]{reservationId}, Locale.ENGLISH)
                ));
    }

    @Transactional
    public void cancelReservationById(MemberDTO.MemberReservationCancelRequest memberReservationCancelRequest) {
        memberReservationCancelRequestObjectValidator.validate(memberReservationCancelRequest);

        MemberReservation memberReservation = memberReservationRepository.findById(memberReservationCancelRequest.getReservationId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        messageSource.getMessage("member.reservation.id.not.found", new Object[]{memberReservationCancelRequest.getReservationId()}, Locale.ENGLISH)
                ));
        if(memberReservation.getStatus().equals(ReservationStatus.CANCELED)) {
            throw new RequestNotAcceptableException(
                    messageSource.getMessage("member.reservation.canceled.already", null, Locale.ENGLISH));
        }
        memberReservation.setStatus(ReservationStatus.CANCELED);

        MemberCourseTicket memberCourseTicket = memberReservation.getMember().getCourseTickets()
                .stream()
                .filter(currentMemberCourseTicket -> currentMemberCourseTicket.getCourseTicket().equals(memberReservation.getCourseClassTime().getCourseClass().getCourseTicket()))
                .toList()
                .get(0);

        memberCourseTicket.setUsedCount(memberCourseTicket.getUsedCount() - 1);

        memberReservationRepository.save(memberReservation);
        memberCourseTicketRepository.save(memberCourseTicket);
    }
}
