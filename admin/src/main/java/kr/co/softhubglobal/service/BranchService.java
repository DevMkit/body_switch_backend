package kr.co.softhubglobal.service;

import kr.co.softhubglobal.dto.branch.BranchDTO;
import kr.co.softhubglobal.dto.branch.BranchInfoMapper;
import kr.co.softhubglobal.entity.branch.*;
import kr.co.softhubglobal.entity.center.Center;
import kr.co.softhubglobal.entity.center.CenterManager;
import kr.co.softhubglobal.exception.customExceptions.DuplicateResourceException;
import kr.co.softhubglobal.exception.customExceptions.ResourceNotFoundException;
import kr.co.softhubglobal.repository.BranchExerciseRoomRepository;
import kr.co.softhubglobal.repository.BranchRepository;
import kr.co.softhubglobal.repository.CenterManagerRepository;
import kr.co.softhubglobal.repository.CenterRepository;
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
    private final CenterManagerRepository centerManagerRepository;
    private final BranchRepository branchRepository;
    private final BranchExerciseRoomRepository branchExerciseRoomRepository;
    private final BranchInfoMapper branchInfoMapper;
    private final MessageSource messageSource;
    private final ObjectValidator<BranchDTO.MainBranchCreateRequest> branchCreateRequestObjectValidator;
    private final ObjectValidator<BranchDTO.BranchExerciseRoomCreateRequest> branchExerciseRoomCreateRequestObjectValidator;

    public List<BranchDTO.BranchInfo> getAllBranches(){
        return branchRepository.findAll()
                .stream()
                .map(branchInfoMapper)
                .toList();
    }

    @Transactional
    public void createMainBranch(BranchDTO.MainBranchCreateRequest mainBranchCreateRequest) {
        branchCreateRequestObjectValidator.validate(mainBranchCreateRequest);

        Center center = centerRepository.findById(mainBranchCreateRequest.getCenterId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        messageSource.getMessage("center.id.not.exist", new Object[]{mainBranchCreateRequest.getCenterId()}, Locale.ENGLISH)));

        CenterManager centerManager = centerManagerRepository.findByCenter(center)
                .orElseThrow(() -> new ResourceNotFoundException(
                        messageSource.getMessage("center.manager.not.exist", new Object[]{mainBranchCreateRequest.getCenterId()}, Locale.ENGLISH)));

        if(branchRepository.existsByCenterAndBranchType(center, BranchType.MAIN)) {
            throw new DuplicateResourceException(
                    messageSource.getMessage("center.main.branch.exist" , null, Locale.ENGLISH));
        }

        Branch branch = new Branch();
        branch.setCenter(center);
        branch.setBranchName(mainBranchCreateRequest.getBranchName());
        branch.setBranchDescription(mainBranchCreateRequest.getBranchDescription());

        if(mainBranchCreateRequest.isAddressSame()) {
            branch.setPostalCode(center.getPostalCode());
            branch.setCity(center.getCity());
            branch.setAddress(center.getAddress());
            branch.setAddressDetail(center.getAddressDetail());
        } else {
            branch.setPostalCode(mainBranchCreateRequest.getPostalCode());
            branch.setCity(mainBranchCreateRequest.getCity());
            branch.setAddress(mainBranchCreateRequest.getAddress());
            branch.setAddressDetail(mainBranchCreateRequest.getAddressDetail());
        }

        branch.setBranchWorkHoursList(
                mainBranchCreateRequest.getWorkHoursInfoList()
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
                mainBranchCreateRequest.getFacilityInfoList()
                        .stream()
                        .map(facilityInfo -> BranchFacility.builder()
                                .branch(branch)
                                .name(facilityInfo.getName())
                                .build())
                        .toList()
        );
        branch.setBranchType(BranchType.MAIN);
        centerManager.setBranch(branch);

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
}
