package kr.co.softhubglobal.service;

import kr.co.softhubglobal.dto.center.CenterDTO;
import kr.co.softhubglobal.dto.center.CenterManagerInfoMapper;
import kr.co.softhubglobal.entity.center.Center;
import kr.co.softhubglobal.entity.center.CenterManager;
import kr.co.softhubglobal.entity.center.CenterStatus;
import kr.co.softhubglobal.entity.center.ManagerType;
import kr.co.softhubglobal.entity.user.Role;
import kr.co.softhubglobal.entity.user.User;
import kr.co.softhubglobal.exception.customExceptions.DuplicateResourceException;
import kr.co.softhubglobal.repository.CenterManagerRepository;
import kr.co.softhubglobal.repository.UserRepository;
import kr.co.softhubglobal.validator.ObjectValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class CenterService {

    private final UserRepository userRepository;
    private final CenterManagerRepository centerManagerRepository;
    private final CenterManagerInfoMapper centerManagerInfoMapper;
    private final ObjectValidator<CenterDTO.CenterManagerCreateInfo> centerManagerCreateInfoObjectValidator;
    private final ObjectValidator<CenterDTO.CenterCreateInfo> centerCreateInfoObjectValidator;
    private final PasswordEncoder passwordEncoder;
    private final MessageSource messageSource;

    public List<CenterDTO.CenterManagerInfo> getAllCenterManagers(){
        return centerManagerRepository.findAll()
                .stream()
                .map(centerManagerInfoMapper)
                .toList();
    }

    public void createCenterManager(CenterDTO.CenterManagerCreateRequest centerManagerCreateRequest) {

        centerManagerCreateInfoObjectValidator.validate(centerManagerCreateRequest.getManagerInfo());
        centerCreateInfoObjectValidator.validate(centerManagerCreateRequest.getCenterInfo());

        if(userRepository.existsByUsernameAndRoleIn(
                centerManagerCreateRequest.getManagerInfo().getUsername(),
                List.of(Role.SUPERADMIN, Role.OPERATOR, Role.MANAGER, Role.EMPLOYEE))) {
            throw new DuplicateResourceException(
                    messageSource.getMessage("center.manager.username.already.exists", new Object[]{centerManagerCreateRequest.getManagerInfo().getUsername()}, Locale.ENGLISH));
        }

        CenterStatus status = centerManagerCreateRequest.getCenterInfo().getStatus();

        centerManagerRepository.save(
                CenterManager.builder()
                        .user(User.builder()
                                .username(centerManagerCreateRequest.getManagerInfo().getUsername())
                                .name(centerManagerCreateRequest.getManagerInfo().getName())
                                .phoneNumber(centerManagerCreateRequest.getManagerInfo().getPhoneNumber())
                                .email(centerManagerCreateRequest.getManagerInfo().getEmail())
                                .password(passwordEncoder.encode(centerManagerCreateRequest.getManagerInfo().getPassword()))
                                .role(Role.MANAGER)
                                .build()
                        )
                        .center(Center.builder()
                                .businessName(centerManagerCreateRequest.getCenterInfo().getBusinessName())
                                .businessNumber(centerManagerCreateRequest.getCenterInfo().getBusinessNumber())
                                .businessClassification(centerManagerCreateRequest.getCenterInfo().getBusinessClassification())
                                .businessType(centerManagerCreateRequest.getCenterInfo().getBusinessType())
                                .postalCode(centerManagerCreateRequest.getCenterInfo().getPostalCode())
                                .city(centerManagerCreateRequest.getCenterInfo().getCity())
                                .address(centerManagerCreateRequest.getCenterInfo().getAddress())
                                .addressDetail(centerManagerCreateRequest.getCenterInfo().getAddressDetail())
                                .representativeNumber(centerManagerCreateRequest.getCenterInfo().getRepresentativeNumber())
                                .email(centerManagerCreateRequest.getCenterInfo().getEmail())
                                .homepage(centerManagerCreateRequest.getCenterInfo().getHomepage())
                                .status(status != null ? status : CenterStatus.APPROVAL_PENDING)
                                .build()
                        )
                        .managerType(ManagerType.STORE)
                        .build()
        );
    }
}