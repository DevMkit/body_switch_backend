package kr.co.softhubglobal.dto.center;

import kr.co.softhubglobal.entity.branch.Branch;
import kr.co.softhubglobal.entity.center.Center;
import kr.co.softhubglobal.entity.center.CenterType;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class CenterInfoMapper implements Function<Center, CenterDTO.CenterInfo> {

    @Override
    public CenterDTO.CenterInfo apply(Center center) {
        Branch branch = center.getBranch();
        return new CenterDTO.CenterInfo(
                center.getId(),
                branch != null ? branch.getBranchName() : null,
                center.getRepresentativeNumber(),
                center.getCenterManager().getUser().getName(),
                center.getCenterManager().getUser().getPhoneNumber(),
                center.getCenterManager().getUser().getEmail(),
                branch != null ? center.getCenterType() : CenterType.NOT_SET
        );
    }
}
