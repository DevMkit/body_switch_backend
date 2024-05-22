package kr.co.softhubglobal.service;

import kr.co.softhubglobal.dto.store.StoreDTO;
import kr.co.softhubglobal.dto.store.StoreRepresentativeInfoMapper;
import kr.co.softhubglobal.entity.store.Store;
import kr.co.softhubglobal.entity.store.StoreRepresentative;
import kr.co.softhubglobal.entity.store.StoreStatus;
import kr.co.softhubglobal.entity.user.User;
import kr.co.softhubglobal.entity.user.UserType;
import kr.co.softhubglobal.exception.customExceptions.DuplicateResourceException;
import kr.co.softhubglobal.repository.StoreRepresentativeRepository;
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
public class StoreService {

    private final UserRepository userRepository;
    private final StoreRepresentativeRepository storeRepresentativeRepository;
    private final StoreRepresentativeInfoMapper storeRepresentativeInfoMapper;
    private final ObjectValidator<StoreDTO.StoreRepresentativeCreateInfo> storeRepresentativeCreateInfoObjectValidator;
    private final ObjectValidator<StoreDTO.StoreCreateInfo> storeCreateInfoObjectValidator;
    private final PasswordEncoder passwordEncoder;
    private final MessageSource messageSource;

    public List<StoreDTO.StoreRepresentativeInfo> getAllStoreRepresentativeRecord(){
        return storeRepresentativeRepository.findAll()
                .stream()
                .map(storeRepresentativeInfoMapper)
                .toList();
    }

    public void createStoreRepresentativeRecord(StoreDTO.StoreRepresentativeCreateRequest storeRepresentativeCreateRequest) {

        storeRepresentativeCreateInfoObjectValidator.validate(storeRepresentativeCreateRequest.getRepresentativeInfo());
        storeCreateInfoObjectValidator.validate(storeRepresentativeCreateRequest.getStoreInfo());

        if(userRepository.existsById(storeRepresentativeCreateRequest.getRepresentativeInfo().getUsername())) {
            throw new DuplicateResourceException(
                    messageSource.getMessage("store.representative.username.already.exists", new Object[]{storeRepresentativeCreateRequest.getRepresentativeInfo().getUsername()}, Locale.ENGLISH));
        }

        StoreStatus status = storeRepresentativeCreateRequest.getStoreInfo().getStatus();

        storeRepresentativeRepository.save(
                StoreRepresentative.builder()
                        .user(User.builder()
                                .username(storeRepresentativeCreateRequest.getRepresentativeInfo().getUsername())
                                .name(storeRepresentativeCreateRequest.getRepresentativeInfo().getName())
                                .phoneNumber(storeRepresentativeCreateRequest.getRepresentativeInfo().getPhoneNumber())
                                .email(storeRepresentativeCreateRequest.getRepresentativeInfo().getEmail())
                                .password(passwordEncoder.encode(storeRepresentativeCreateRequest.getRepresentativeInfo().getPassword()))
                                .userType(UserType.STORE_REPRESENTATIVE)
                                .build()
                        )
                        .store(Store.builder()
                                .businessName(storeRepresentativeCreateRequest.getStoreInfo().getBusinessName())
                                .businessNumber(storeRepresentativeCreateRequest.getStoreInfo().getBusinessNumber())
                                .businessClassification(storeRepresentativeCreateRequest.getStoreInfo().getBusinessClassification())
                                .businessType(storeRepresentativeCreateRequest.getStoreInfo().getBusinessType())
                                .postalCode(storeRepresentativeCreateRequest.getStoreInfo().getPostalCode())
                                .city(storeRepresentativeCreateRequest.getStoreInfo().getCity())
                                .address(storeRepresentativeCreateRequest.getStoreInfo().getAddress())
                                .addressDetail(storeRepresentativeCreateRequest.getStoreInfo().getAddressDetail())
                                .representativeNumber(storeRepresentativeCreateRequest.getStoreInfo().getRepresentativeNumber())
                                .email(storeRepresentativeCreateRequest.getStoreInfo().getEmail())
                                .homepage(storeRepresentativeCreateRequest.getStoreInfo().getHomepage())
                                .status(status != null ? status : StoreStatus.APPROVAL_PENDING)
                                .build()
                        )
                        .build()
        );
    }
}
