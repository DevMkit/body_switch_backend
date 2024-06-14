package kr.co.softhubglobal.dto.branch;

import kr.co.softhubglobal.entity.branch.Branch;
import kr.co.softhubglobal.entity.branch.BranchExerciseRoom;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class BranchExerciseRoomInfoMapper implements Function<BranchExerciseRoom, BranchDTO.BranchExerciseRoomInfo> {

    @Override
    public BranchDTO.BranchExerciseRoomInfo apply(BranchExerciseRoom branchExerciseRoom) {
        return new BranchDTO.BranchExerciseRoomInfo(
                branchExerciseRoom.getId(),
                branchExerciseRoom.getName()
        );
    }
}
