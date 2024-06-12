package kr.co.softhubglobal.service;

import kr.co.softhubglobal.dto.center.CenterDTO;
import kr.co.softhubglobal.dto.center.CenterManagerDetailInfoMapper;
import kr.co.softhubglobal.dto.center.CenterManagerInfoMapper;
import kr.co.softhubglobal.entity.center.Center;
import kr.co.softhubglobal.entity.center.CenterManager;
import kr.co.softhubglobal.entity.center.CenterManagerStatus;
import kr.co.softhubglobal.entity.center.ManagerType;
import kr.co.softhubglobal.entity.common.Restrictions;
import kr.co.softhubglobal.entity.user.Role;
import kr.co.softhubglobal.entity.user.User;
import kr.co.softhubglobal.exception.customExceptions.DuplicateResourceException;
import kr.co.softhubglobal.exception.customExceptions.ResourceNotFoundException;
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
public class CenterManagerService {

    private final UserRepository userRepository;
    private final CenterManagerRepository centerManagerRepository;
    private final CenterManagerInfoMapper centerManagerInfoMapper;
    private final CenterManagerDetailInfoMapper centerManagerDetailInfoMapper;
    private final ObjectValidator<CenterDTO.CenterManagerCreateInfo> centerManagerCreateInfoObjectValidator;
    private final ObjectValidator<CenterDTO.CenterCreateInfo> centerCreateInfoObjectValidator;
    private final PasswordEncoder passwordEncoder;
    private final MessageSource messageSource;

    public List<CenterDTO.CenterManagerInfo> getAllCenterManagers(CenterDTO.CenterManagerSearchRequest searchRequest){
        Restrictions restrictions = new Restrictions();
        if(searchRequest.getBranchId() != null) {
            restrictions.eq("branch.id", searchRequest.getBranchId());
        }
        if(searchRequest.getName() != null) {
            restrictions.like("user.name", "%" + searchRequest.getName() + "%");
        }
        if(searchRequest.getRegisteredDateFrom() != null && searchRequest.getRegisteredDateTo() != null){
            restrictions.between("registeredDate", searchRequest.getRegisteredDateFrom().atStartOfDay(), searchRequest.getRegisteredDateTo().atTime(23, 59, 59) );
        }
        if(searchRequest.getStatus() != null) {
            restrictions.eq("center.status", searchRequest.getStatus());
        }
        return centerManagerRepository.findAll(restrictions.output())
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
                                .build()
                        )
                        .status(CenterManagerStatus.APPROVAL_PENDING)
                        .build()
        );
    }

    public CenterDTO.CenterManagerDetailInfo getCenterManagerById(Long centerManagerId) {
        return centerManagerRepository.findById(centerManagerId)
                .map(centerManagerDetailInfoMapper)
                .orElseThrow(() -> new ResourceNotFoundException(
                        messageSource.getMessage("center.manager.id.not.exist", new Object[]{centerManagerId}, Locale.ENGLISH)));
    }
}
