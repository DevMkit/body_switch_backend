package kr.co.softhubglobal.dto.branch;

import kr.co.softhubglobal.dto.center.CenterDTO;
import kr.co.softhubglobal.entity.branch.BranchProductCategory;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class BranchProductCategoryInfoMapper implements Function<BranchProductCategory, CenterDTO.BranchProductCategoryInfo> {

    @Override
    public CenterDTO.BranchProductCategoryInfo apply(BranchProductCategory branchProductCategory) {
        return new CenterDTO.BranchProductCategoryInfo(
                branchProductCategory.getId(),
                branchProductCategory.getName()
        );
    }
}
