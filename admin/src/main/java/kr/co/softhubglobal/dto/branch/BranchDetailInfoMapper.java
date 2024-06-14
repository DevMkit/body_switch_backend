package kr.co.softhubglobal.dto.branch;

import kr.co.softhubglobal.entity.branch.Branch;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class BranchDetailInfoMapper implements Function<Branch, BranchDTO.BranchDetailInfo> {

    @Override
    public BranchDTO.BranchDetailInfo apply(Branch branch) {
        return new BranchDTO.BranchDetailInfo(
                branch.getId(),
                branch.getBranchName(),
                branch.getBranchDescription(),
                branch.getAddress(),
                branch.getBranchWorkHoursList().stream().map(
                        branchWorkHour -> new BranchDTO.WorkHoursInfo(
                                branchWorkHour.getIsOpen(),
                                branchWorkHour.getDayOfWeek(),
                                branchWorkHour.getOpenTime(),
                                branchWorkHour.getCloseTime()
                        )
                ).toList(),
                branch.getReservationPolicy(),
                branch.getBranchFacilities().stream().map(
                        branchFacility -> new BranchDTO.FacilityInfo(
                                branchFacility.getName()
                        )
                ).toList(),
                branch.getBranchDetailDescription()
        );
    }
}
