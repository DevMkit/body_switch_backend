package kr.co.softhubglobal.dto.center;

import kr.co.softhubglobal.entity.branch.Branch;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class CenterBranchInfoMapper implements Function<Branch, CenterDTO.CenterBranchInfo> {

    @Override
    public CenterDTO.CenterBranchInfo apply(Branch branch) {
        return new CenterDTO.CenterBranchInfo(
                branch.getCenter().getId(),
                branch.getId(),
                branch.getBranchName(),
                branch.getCenter().getRepresentativeNumber(),
                branch.getCenterManager().getUser().getName(),
                branch.getCenterManager().getUser().getPhoneNumber(),
                branch.getCenterManager().getUser().getEmail(),
                branch.getBranchType()
        );
    }
}
