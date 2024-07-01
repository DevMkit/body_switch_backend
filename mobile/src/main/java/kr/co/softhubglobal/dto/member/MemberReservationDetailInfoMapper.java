package kr.co.softhubglobal.dto.member;

import kr.co.softhubglobal.entity.member.MemberReservation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class MemberReservationDetailInfoMapper implements Function<MemberReservation, MemberDTO.MemberReservationDetailInfo> {

    private final MemberCourseTicketInfoMapper memberCourseTicketInfoMapper;

    @Override
    public MemberDTO.MemberReservationDetailInfo apply(MemberReservation memberReservation) {

        return new MemberDTO.MemberReservationDetailInfo(
                memberReservation.getId(),
                memberReservation.getCourseClassTime().getCourseClass().getClassType(),
                memberReservation.getCourseClassTime().getCourseClass().getBranch().getBranchName(),
                memberReservation.getCourseClassTime().getCourseClass().getClassName(),
                memberReservation.getReservationDate(),
                memberReservation.getCourseClassTime().getStartTime(),
                memberReservation.getCourseClassTime().getEndTime(),
                memberReservation.getCourseClassTime().getCourseClass().getExerciseRoom().getName(),
                memberReservation.getCourseClassTime().getCourseClass().getEmployee().getUser().getName(),
                memberCourseTicketInfoMapper.apply(
                        memberReservation.getMember().getCourseTickets()
                                .stream()
                                .filter(memberCourseTicket -> memberCourseTicket.getCourseTicket().equals(memberReservation.getCourseClassTime().getCourseClass().getCourseTicket()))
                                .toList()
                                .get(0)
                )
        );
    }
}
