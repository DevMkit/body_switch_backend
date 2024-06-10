package kr.co.softhubglobal.dto.branch;

import kr.co.softhubglobal.entity.branch.Branch;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class BranchInfoMapper implements Function<Branch, BranchDTO.BranchInfo> {

    @Override
    public BranchDTO.BranchInfo apply(Branch branch) {
        return new BranchDTO.BranchInfo(
                branch.getId(),
                branch.getBranchName(),
                branch.getCenter().getBusinessName()
        );
    }
}
