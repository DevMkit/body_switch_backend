package kr.co.softhubglobal.dto.center;

import kr.co.softhubglobal.dto.branch.BranchDetailInfoMapper;
import kr.co.softhubglobal.dto.branch.BranchExerciseRoomInfoMapper;
import kr.co.softhubglobal.dto.branch.BranchProductCategoryInfoMapper;
import kr.co.softhubglobal.dto.branch.BranchProductInfoMapper;
import kr.co.softhubglobal.entity.center.Center;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class CenterDetailInfoMapper implements Function<Center, CenterDTO.CenterDetailInfo> {

    private final CenterManagerDetailInfoMapper centerManagerDetailInfoMapper;
    private final BranchDetailInfoMapper branchDetailInfoMapper;
    private final BranchExerciseRoomInfoMapper branchExerciseRoomInfoMapper;
    private final BranchProductCategoryInfoMapper branchProductCategoryInfoMapper;
    private final BranchProductInfoMapper branchProductInfoMapper;

    @Override
    public CenterDTO.CenterDetailInfo apply(Center center) {
        return new CenterDTO.CenterDetailInfo(
                centerManagerDetailInfoMapper.apply(center.getCenterManager()),
                center.getBranch() != null ? branchDetailInfoMapper.apply(center.getBranch()) : null,
                center.getBranch() != null ? center.getBranch().getBranchExerciseRooms().stream().map(branchExerciseRoomInfoMapper).toList() : new ArrayList<>(),
                center.getBranch() != null ? center.getBranch().getBranchProductCategories().stream().map(branchProductCategoryInfoMapper).toList() : new ArrayList<>(),
                center.getBranch() != null ? center.getBranch().getBranchProducts().stream().map(branchProductInfoMapper).toList() : new ArrayList<>()
        );
    }
}
