package kr.co.softhubglobal.dto.center;

import kr.co.softhubglobal.entity.center.CenterManager;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class CenterManagerDetailInfoMapper implements Function<CenterManager, CenterDTO.CenterManagerDetailInfo> {

    @Override
    public CenterDTO.CenterManagerDetailInfo apply(CenterManager centerManager) {
        return new CenterDTO.CenterManagerDetailInfo(
                new CenterDTO.ManagerInfo(
                        centerManager.getId(),
                        centerManager.getUser().getName(),
                        centerManager.getUser().getPhoneNumber(),
                        centerManager.getUser().getUsername(),
                        centerManager.getUser().getEmail(),
                        centerManager.getStatus()
                ),
                new CenterDTO.BusinessInfo(
                        centerManager.getCenter().getBusinessName(),
                        centerManager.getCenter().getBusinessNumber(),
                        centerManager.getCenter().getRepresentativeNumber(),
                        centerManager.getCenter().getEmail(),
                        centerManager.getCenter().getBusinessClassification(),
                        centerManager.getCenter().getHomepage(),
                        centerManager.getCenter().getBusinessType(),
                        centerManager.getCenter().getAddress(),
                        centerManager.getCenter().getAddressDetail()
                )
        );
    }
}
