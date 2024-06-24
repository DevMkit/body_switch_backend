package kr.co.softhubglobal.service;

import kr.co.softhubglobal.dto.PageableDTO;
import kr.co.softhubglobal.dto.center.CenterDTO;
import kr.co.softhubglobal.dto.center.CenterManagerDetailInfoMapper;
import kr.co.softhubglobal.dto.center.CenterManagerInfoMapper;
import kr.co.softhubglobal.entity.center.*;
import kr.co.softhubglobal.entity.common.Restrictions;
import kr.co.softhubglobal.entity.user.Role;
import kr.co.softhubglobal.entity.user.User;
import kr.co.softhubglobal.exception.customExceptions.DuplicateResourceException;
import kr.co.softhubglobal.exception.customExceptions.RequestNotAcceptableException;
import kr.co.softhubglobal.exception.customExceptions.RequestValidationException;
import kr.co.softhubglobal.exception.customExceptions.ResourceNotFoundException;
import kr.co.softhubglobal.repository.CenterManagerRepository;
import kr.co.softhubglobal.repository.CenterRepository;
import kr.co.softhubglobal.repository.UserRepository;
import kr.co.softhubglobal.validator.ObjectValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CenterManagerService {

    private final UserRepository userRepository;
    private final CenterRepository centerRepository;
    private final CenterManagerRepository centerManagerRepository;
    private final CenterManagerInfoMapper centerManagerInfoMapper;
    private final CenterManagerDetailInfoMapper centerManagerDetailInfoMapper;
    private final ObjectValidator<CenterDTO.CenterManagerCreateInfo> centerManagerCreateInfoObjectValidator;
    private final ObjectValidator<CenterDTO.CenterCreateInfo> centerCreateInfoObjectValidator;
    private final ObjectValidator<CenterDTO.CenterManagerUpdateInfo> centerManagerUpdateInfoObjectValidator;
    private final ObjectValidator<CenterDTO.CenterUpdateInfo> centerUpdateInfoObjectValidator;
    private final PasswordEncoder passwordEncoder;
    private final MessageSource messageSource;

    public PageableDTO.Response getAllCenterManagers(CenterDTO.CenterManagerSearchRequest centerManagerSearchRequest){

        Restrictions restrictions = new Restrictions();
        if(centerManagerSearchRequest.getCenterId() != null) {
            restrictions.eq("center.id", centerManagerSearchRequest.getCenterId());
        }
        if(centerManagerSearchRequest.getName() != null) {
            restrictions.like("user.name", "%" + centerManagerSearchRequest.getName() + "%");
        }
        if(centerManagerSearchRequest.getRegisteredDateFrom() != null && centerManagerSearchRequest.getRegisteredDateTo() != null){
            restrictions.between("registeredDate", centerManagerSearchRequest.getRegisteredDateFrom().atStartOfDay(), centerManagerSearchRequest.getRegisteredDateTo().atTime(23, 59, 59) );
        }
        if(centerManagerSearchRequest.getStatus() != null) {
            restrictions.eq("status", centerManagerSearchRequest.getStatus());
        }

        PageRequest pageRequest = PageRequest.of(
                centerManagerSearchRequest.getPage() - 1,
                centerManagerSearchRequest.getLimit(),
                Sort.by("registeredDate").descending()
        );
        Page<CenterManager> result = centerManagerRepository.findAll(
                restrictions.output(),
                pageRequest
        );

        PageableDTO.Response response = new PageableDTO.Response();
        response.setTotalElements(result.getTotalElements());
        response.setNumber(result.getNumber() + 1);
        response.setSize(result.getSize());
        response.setContent(
                result.getContent()
                        .stream()
                        .map(centerManagerInfoMapper)
                        .toList()
        );
        return response;
    }

    public void createCenterManager(
            CenterDTO.CenterManagerCreateRequest centerManagerCreateRequest,
            CenterManagerStatus status,
            CenterType centerType,
            Long userId
    ) {
        centerManagerCreateInfoObjectValidator.validate(centerManagerCreateRequest.getManagerInfo());
        centerCreateInfoObjectValidator.validate(centerManagerCreateRequest.getCenterInfo());

        if(userRepository.existsByUsernameAndRoleIn(
                centerManagerCreateRequest.getManagerInfo().getUsername(),
                List.of(Role.SUPERADMIN, Role.OPERATOR, Role.MANAGER))
        ) {
            throw new DuplicateResourceException(
                    messageSource.getMessage("center.manager.username.already.exists", new Object[]{centerManagerCreateRequest.getManagerInfo().getUsername()}, Locale.ENGLISH));
        }

        String stringValueUserId = null;
        if(centerManagerCreateRequest.getStatus() != null) {
            status = centerManagerCreateRequest.getStatus();
        }
        if(status == null) {
            throw new RequestNotAcceptableException(
                    messageSource.getMessage("center.status.not.null", null, Locale.ENGLISH));
        }
        if (userId != null) {
            stringValueUserId = String.valueOf(userId);
            if (centerType != null && centerType.equals(CenterType.BRANCH)) {
                if(centerManagerCreateRequest.getHeadCenterId() == null) {
                    throw new RequestValidationException(
                            messageSource.getMessage("head.center.id.null", null, Locale.ENGLISH));
                }
                Center center = centerRepository.findById(centerManagerCreateRequest.getHeadCenterId())
                        .orElseThrow(() -> new ResourceNotFoundException(
                                messageSource.getMessage("center.id.not.exist", new Object[]{centerManagerCreateRequest.getHeadCenterId()}, Locale.ENGLISH)));
                if(!center.getCenterType().equals(CenterType.HEAD)) {
                    throw new RequestNotAcceptableException(
                            messageSource.getMessage("center.not.head.branch", null, Locale.ENGLISH));
                }
                if(!Objects.equals(center.getCenterManager().getUser().getId(), userId)) {
                    throw new RequestNotAcceptableException(
                            messageSource.getMessage("center.manager.not.match", null, Locale.ENGLISH));
                }
            }
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
                                .centerType(centerType)
                                .headCenterId(centerManagerCreateRequest.getHeadCenterId())
                                .registeredId(stringValueUserId)
                                .updatedId(stringValueUserId)
                                .build()
                        )
                        .status(status)
                        .registeredId(stringValueUserId)
                        .updatedId(stringValueUserId)
                        .build()
        );
    }

    public CenterDTO.CenterManagerDetailInfo getCenterManagerDetailInfoById(Long centerManagerId) {
        return centerManagerRepository.findById(centerManagerId)
                .map(centerManagerDetailInfoMapper)
                .orElseThrow(() -> new ResourceNotFoundException(
                        messageSource.getMessage("center.manager.id.not.exist", new Object[]{centerManagerId}, Locale.ENGLISH)));
    }

    @Transactional
    public void updateCenterManagerById(
            Long centerManagerId,
            CenterDTO.CenterManagerUpdateRequest centerManagerUpdateRequest,
            Long userId
    ) {
        centerManagerUpdateInfoObjectValidator.validate(centerManagerUpdateRequest.getManagerInfo());
        centerUpdateInfoObjectValidator.validate(centerManagerUpdateRequest.getCenterInfo());

        CenterManager centerManager = centerManagerRepository.findById(centerManagerId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        messageSource.getMessage("center.manager.id.not.exist", new Object[]{centerManagerId}, Locale.ENGLISH)));

        if(centerManagerUpdateRequest.getManagerInfo() != null) {
            if(centerManagerUpdateRequest.getManagerInfo().getName() != null) {
                centerManager.getUser().setName(centerManagerUpdateRequest.getManagerInfo().getName());
            }
            if(centerManagerUpdateRequest.getManagerInfo().getPassword() != null) {
                centerManager.getUser().setPassword(passwordEncoder.encode(centerManagerUpdateRequest.getManagerInfo().getPassword()));
            }
            if(centerManagerUpdateRequest.getManagerInfo().getPhoneNumber() != null) {
                centerManager.getUser().setPhoneNumber(centerManagerUpdateRequest.getManagerInfo().getPhoneNumber());
            }
            if(centerManagerUpdateRequest.getManagerInfo().getEmail() != null) {
                centerManager.getUser().setEmail(centerManagerUpdateRequest.getManagerInfo().getEmail());
            }
        }
        if(centerManagerUpdateRequest.getCenterInfo() != null) {
            if(centerManagerUpdateRequest.getCenterInfo().getBusinessName() != null) {
                centerManager.getCenter().setBusinessName(centerManagerUpdateRequest.getCenterInfo().getBusinessName());
            }
            if(centerManagerUpdateRequest.getCenterInfo().getBusinessNumber() != null) {
                centerManager.getCenter().setBusinessNumber(centerManagerUpdateRequest.getCenterInfo().getBusinessNumber());
            }
            if(centerManagerUpdateRequest.getCenterInfo().getBusinessClassification() != null) {
                centerManager.getCenter().setBusinessClassification(centerManagerUpdateRequest.getCenterInfo().getBusinessClassification());
            }
            if(centerManagerUpdateRequest.getCenterInfo().getBusinessType() != null) {
                centerManager.getCenter().setBusinessType(centerManagerUpdateRequest.getCenterInfo().getBusinessType());
            }
            if(centerManagerUpdateRequest.getCenterInfo().getPostalCode() != null) {
                centerManager.getCenter().setPostalCode(centerManagerUpdateRequest.getCenterInfo().getPostalCode());
            }
            if(centerManagerUpdateRequest.getCenterInfo().getCity() != null) {
                centerManager.getCenter().setCity(centerManagerUpdateRequest.getCenterInfo().getCity());
            }
            if(centerManagerUpdateRequest.getCenterInfo().getAddress() != null) {
                centerManager.getCenter().setAddress(centerManagerUpdateRequest.getCenterInfo().getAddress());
            }
            if(centerManagerUpdateRequest.getCenterInfo().getAddressDetail() != null) {
                centerManager.getCenter().setAddressDetail(centerManagerUpdateRequest.getCenterInfo().getAddressDetail());
            }
            if(centerManagerUpdateRequest.getCenterInfo().getRepresentativeNumber() != null) {
                centerManager.getCenter().setRepresentativeNumber(centerManagerUpdateRequest.getCenterInfo().getRepresentativeNumber());
            }
            if(centerManagerUpdateRequest.getCenterInfo().getEmail() != null) {
                centerManager.getCenter().setEmail(centerManagerUpdateRequest.getCenterInfo().getEmail());
            }
            if(centerManagerUpdateRequest.getCenterInfo().getHomepage() != null) {
                centerManager.getCenter().setHomepage(centerManagerUpdateRequest.getCenterInfo().getHomepage());
            }
        }
        if(centerManagerUpdateRequest.getStatus() != null) {
           centerManager.setStatus(centerManagerUpdateRequest.getStatus());
        }
        centerManager.setUpdatedId(String.valueOf(userId));
        centerManagerRepository.save(centerManager);
    }
}
