package kr.co.softhubglobal.dto.branch;

import kr.co.softhubglobal.dto.center.CenterDTO;
import kr.co.softhubglobal.entity.branch.BranchProduct;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class BranchProductInfoMapper implements Function<BranchProduct, CenterDTO.BranchProductInfo> {

    @Override
    public CenterDTO.BranchProductInfo apply(BranchProduct branchProduct) {
        return new CenterDTO.BranchProductInfo(
                branchProduct.getId(),
                branchProduct.getBranchCategoryId(),
                branchProduct.getName(),
                branchProduct.getPrice(),
                branchProduct.getStockQuantity()
        );
    }
}
