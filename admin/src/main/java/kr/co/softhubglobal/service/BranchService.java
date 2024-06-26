package kr.co.softhubglobal.service;

import kr.co.softhubglobal.dto.branch.BranchDTO;
import kr.co.softhubglobal.dto.branch.BranchInfoMapper;
import kr.co.softhubglobal.entity.branch.*;
import kr.co.softhubglobal.entity.center.Center;
import kr.co.softhubglobal.exception.customExceptions.DuplicateResourceException;
import kr.co.softhubglobal.exception.customExceptions.ResourceNotFoundException;
import kr.co.softhubglobal.repository.*;
import kr.co.softhubglobal.validator.ObjectValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class BranchService {

    private final CenterRepository centerRepository;
    private final BranchRepository branchRepository;
    private final BranchExerciseRoomRepository branchExerciseRoomRepository;
    private final BranchProductCategoryRepository branchProductCategoryRepository;
    private final BranchProductRepository branchProductRepository;
    private final BranchInfoMapper branchInfoMapper;
    private final MessageSource messageSource;
    private final ObjectValidator<BranchDTO.BranchCreateRequest> branchCreateRequestObjectValidator;
    private final ObjectValidator<BranchDTO.BranchExerciseRoomCreateRequest> branchExerciseRoomCreateRequestObjectValidator;
    private final ObjectValidator<BranchDTO.BranchProductCategoryCreateRequest> branchProductCategoryCreateRequestObjectValidator;
    private final ObjectValidator<BranchDTO.BranchProductCreateRequest> branchProductCreateRequestObjectValidator;

    public List<BranchDTO.BranchInfo> getAllBranches(){
        return branchRepository.findAll()
                .stream()
                .map(branchInfoMapper)
                .toList();
    }

    @Transactional
    public void createBranch(BranchDTO.BranchCreateRequest branchCreateRequest) {
        branchCreateRequestObjectValidator.validate(branchCreateRequest);

        Center center = centerRepository.findById(branchCreateRequest.getCenterId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        messageSource.getMessage("center.id.not.exist", new Object[]{branchCreateRequest.getCenterId()}, Locale.ENGLISH)));

        if(branchRepository.existsByCenter(center)) {
            throw new DuplicateResourceException(
                    messageSource.getMessage("center.main.branch.exist" , null, Locale.ENGLISH));
        }

        Branch branch = new Branch();
        branch.setCenter(center);
        branch.setBranchName(branchCreateRequest.getBranchName());
        branch.setBranchDescription(branchCreateRequest.getBranchDescription());
        branch.setReservationPolicy(branchCreateRequest.getReservationPolicy());
        branch.setBranchDetailDescription(branchCreateRequest.getBranchDetailDescription());

        if(branchCreateRequest.isAddressSame()) {
            branch.setPostalCode(center.getPostalCode());
            branch.setCity(center.getCity());
            branch.setAddress(center.getAddress());
            branch.setAddressDetail(center.getAddressDetail());
        } else {
            branch.setPostalCode(branchCreateRequest.getPostalCode());
            branch.setCity(branchCreateRequest.getCity());
            branch.setAddress(branchCreateRequest.getAddress());
            branch.setAddressDetail(branchCreateRequest.getAddressDetail());
        }

        branch.setBranchWorkHoursList(
                branchCreateRequest.getWorkHoursInfoList()
                        .stream()
                        .map(workHoursInfo -> BranchWorkHours.builder()
                                .branch(branch)
                                .dayOfWeek(workHoursInfo.getDayOfWeek())
                                .isOpen(workHoursInfo.isOpen())
                                .openTime(workHoursInfo.getOpenTime())
                                .closeTime(workHoursInfo.getCloseTime())
                                .build())
                        .toList()
        );

        branch.setBranchFacilities(
                branchCreateRequest.getFacilityInfoList()
                        .stream()
                        .map(facilityInfo -> BranchFacility.builder()
                                .branch(branch)
                                .name(facilityInfo.getFacilityName())
                                .build())
                        .toList()
        );

        branchRepository.save(branch);
    }

    @Transactional
    public void updateBranchById(Long branchId, BranchDTO.BranchUpdateRequest branchUpdateRequest) {

        Branch branch = branchRepository.findById(branchId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        messageSource.getMessage("branch.id.not.exist", new Object[]{branchId}, Locale.ENGLISH)));

        if(branchUpdateRequest.getBranchName() != null) {
            branch.setBranchName(branchUpdateRequest.getBranchName());
        }
        if(branchUpdateRequest.getBranchDescription() != null) {
            branch.setBranchDescription(branchUpdateRequest.getBranchDescription());
        }
        if(branchUpdateRequest.isAddressSame()) {
            branch.setPostalCode(branch.getCenter().getPostalCode());
            branch.setCity(branch.getCenter().getCity());
            branch.setAddress(branch.getCenter().getAddress());
            branch.setAddressDetail(branch.getCenter().getAddressDetail());
        } else {
            branch.setPostalCode(branchUpdateRequest.getPostalCode());
            branch.setCity(branchUpdateRequest.getCity());
            branch.setAddress(branchUpdateRequest.getAddress());
            branch.setAddressDetail(branchUpdateRequest.getAddressDetail());
        }
        if(branchUpdateRequest.getWorkHoursInfoList() != null && !branchUpdateRequest.getWorkHoursInfoList().isEmpty()) {
            branch.getBranchWorkHoursList().clear();
            branch.getBranchWorkHoursList().addAll(
                    branchUpdateRequest.getWorkHoursInfoList()
                            .stream()
                            .map(workHoursInfo -> BranchWorkHours.builder()
                                    .branch(branch)
                                    .dayOfWeek(workHoursInfo.getDayOfWeek())
                                    .isOpen(workHoursInfo.isOpen())
                                    .openTime(workHoursInfo.getOpenTime())
                                    .closeTime(workHoursInfo.getCloseTime())
                                    .build())
                            .toList()
            );
        }
        if(branchUpdateRequest.getReservationPolicy() != null) {
            branch.setReservationPolicy(branchUpdateRequest.getReservationPolicy());
        }
        if(branchUpdateRequest.getFacilityInfoList() != null && !branchUpdateRequest.getFacilityInfoList().isEmpty()) {
            branch.getBranchFacilities().clear();
            branch.getBranchFacilities().addAll(
                    branchUpdateRequest.getFacilityInfoList()
                            .stream()
                            .map(facilityInfo -> BranchFacility.builder()
                                    .branch(branch)
                                    .name(facilityInfo.getFacilityName())
                                    .build())
                            .toList()
            );
        }
        if(branchUpdateRequest.getBranchDetailDescription() != null) {
            branch.setBranchDetailDescription(branchUpdateRequest.getBranchDetailDescription());
        }

        branchRepository.save(branch);
    }

    public void createBranchExerciseRoom(Long branchId, BranchDTO.BranchExerciseRoomCreateRequest branchExerciseRoomCreateRequest) {

        branchExerciseRoomCreateRequestObjectValidator.validate(branchExerciseRoomCreateRequest);

        Branch branch = branchRepository.findById(branchId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        messageSource.getMessage("branch.id.not.exist", new Object[]{branchId}, Locale.ENGLISH)));

        branchExerciseRoomRepository.save(
                BranchExerciseRoom.builder()
                        .branch(branch)
                        .name(branchExerciseRoomCreateRequest.getRoomName())
                        .build()
        );
    }

    public void createBranchProductCategory(Long branchId, BranchDTO.BranchProductCategoryCreateRequest branchProductCategoryCreateRequest) {
        branchProductCategoryCreateRequestObjectValidator.validate(branchProductCategoryCreateRequest);

        Branch branch = branchRepository.findById(branchId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        messageSource.getMessage("branch.id.not.exist", new Object[]{branchId}, Locale.ENGLISH)));

        branchProductCategoryRepository.save(
                BranchProductCategory.builder()
                        .branch(branch)
                        .name(branchProductCategoryCreateRequest.getCategoryName())
                        .build()
        );
    }

    public void createBranchProduct(Long branchId, BranchDTO.BranchProductCreateRequest branchProductCreateRequest) {
        branchProductCreateRequestObjectValidator.validate(branchProductCreateRequest);

        Branch branch = branchRepository.findById(branchId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        messageSource.getMessage("branch.id.not.exist", new Object[]{branchId}, Locale.ENGLISH)));

        branchProductRepository.save(
                BranchProduct.builder()
                        .branch(branch)
                        .branchCategoryId(branchProductCreateRequest.getCategoryId())
                        .name(branchProductCreateRequest.getProductName())
                        .price(branchProductCreateRequest.getPrice())
                        .stockQuantity(branchProductCreateRequest.getQuantity())
                        .build()
        );
    }
}
