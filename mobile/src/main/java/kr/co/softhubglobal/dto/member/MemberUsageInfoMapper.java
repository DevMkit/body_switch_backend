package kr.co.softhubglobal.dto.member;

import kr.co.softhubglobal.entity.member.Member;
import kr.co.softhubglobal.entity.member.MemberCourseTicketStatus;
import kr.co.softhubglobal.entity.member.ReservationStatus;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class MemberUsageInfoMapper implements Function<Member, MemberDTO.MemberUsageInfo> {

    @Override
    public MemberDTO.MemberUsageInfo apply(Member member) {
        return new MemberDTO.MemberUsageInfo(
                member.getReservations()
                        .stream()
                        .filter(memberReservation -> !memberReservation.getStatus().equals(ReservationStatus.CANCELED))
                        .toList()
                        .size(),
                member.getCourseTickets()
                        .stream()
                        .filter(memberCourseTicket -> memberCourseTicket.getStatus().equals(MemberCourseTicketStatus.ACTIVE))
                        .toList()
                        .size(),
                0
        );
    }
}
