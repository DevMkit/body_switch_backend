package kr.co.softhubglobal.dto.member;

import kr.co.softhubglobal.entity.member.MemberReservation;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class MemberReservationInfoMapper implements Function<MemberReservation, MemberDTO.MemberReservationInfo> {

    @Override
    public MemberDTO.MemberReservationInfo apply(MemberReservation memberReservation) {
        return new MemberDTO.MemberReservationInfo(
                memberReservation.getId(),
                memberReservation.getReservationDate(),
                memberReservation.getCourseClassTime().getStartTime(),
                memberReservation.getCourseClassTime().getEndTime(),
                memberReservation.getCourseClassTime().getCourseClass().getClassName(),
                memberReservation.getCourseClassTime().getCourseClass().getExerciseRoom().getName(),
                memberReservation.getCourseClassTime().getCourseClass().getEmployee().getUser().getName(),
                memberReservation.getCourseClassTime().getCourseClass().getClassType(),
                memberReservation.getCourseClassTime().getCourseClass().getBranch().getBranchName()
        );
    }
}
