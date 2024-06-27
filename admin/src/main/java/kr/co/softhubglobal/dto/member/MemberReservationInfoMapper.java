package kr.co.softhubglobal.dto.member;

import kr.co.softhubglobal.entity.member.MemberReservation;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.function.Function;

@Service
public class MemberReservationInfoMapper implements Function<MemberReservation, MemberDTO.MemberReservationInfo> {

    @Override
    public MemberDTO.MemberReservationInfo apply(MemberReservation memberReservation) {
        return new MemberDTO.MemberReservationInfo(
                LocalDateTime.of(
                        memberReservation.getReservationDate().with(TemporalAdjusters.previousOrSame(memberReservation.getCourseClassTime().getDayOfWeek())),
                        memberReservation.getCourseClassTime().getStartTime()
                ),
                memberReservation.getRegisteredDate(),
                memberReservation.getCourseClassTime().getCourseClass().getClassName(),
                memberReservation.getCourseClassTime().getCourseClass().getEmployee().getUser().getName()
        );
    }
}
