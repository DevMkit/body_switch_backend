package kr.co.softhubglobal.dto.center;

import kr.co.softhubglobal.entity.center.CenterManager;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class CenterManagerInfoMapper implements Function<CenterManager, CenterDTO.CenterManagerInfo> {

    @Override
    public CenterDTO.CenterManagerInfo apply(CenterManager centerManager) {
        return new CenterDTO.CenterManagerInfo(
                centerManager.getId(),
                centerManager.getCenter().getBusinessName(),
                centerManager.getUser().getUsername(),
                centerManager.getUser().getName(),
                centerManager.getUser().getPhoneNumber(),
                centerManager.getUser().getRegisteredDate(),
                centerManager.getCenter().getStatus()
        );
    }
}
