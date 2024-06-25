package kr.co.softhubglobal.dto.member;

import kr.co.softhubglobal.entity.common.BaseDateEntity;
import kr.co.softhubglobal.entity.member.MemberCourseTicket;
import kr.co.softhubglobal.entity.member.MemberReservation;
import kr.co.softhubglobal.entity.member.ReservationStatus;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

@Service
public class MemberTicketInfoMapper implements Function<MemberCourseTicket, MemberDTO.MemberTicketInfo> {

    @Override
    public MemberDTO.MemberTicketInfo apply(MemberCourseTicket memberCourseTicket) {

        List<MemberReservation> memberReservations = memberCourseTicket.getMember().getReservations()
                .stream()
                .filter(memberReservation -> !memberReservation.getStatus().equals(ReservationStatus.CANCELED))
                .sorted(Comparator.comparing(BaseDateEntity::getRegisteredDate, Comparator.reverseOrder()))
                .toList();

        return new MemberDTO.MemberTicketInfo(
                memberCourseTicket.getCourseTicket().getId(),
                memberCourseTicket.getCourseTicket().getTicketName(),
                memberCourseTicket.getCourseTicket().usageCount,
                memberCourseTicket.getCourseTicket().usagePeriod,
                memberReservations.isEmpty() ? ""
                        : memberReservations.stream().findFirst().get().getCourseClassTime().getCourseClass().getEmployee().getUser().getName(),
                memberCourseTicket.getCourseTicket().usageCount - memberCourseTicket.getUsedCount(),
                memberCourseTicket.getExpireDate(),
                memberReservations
                        .stream()
                        .filter(memberReservation -> memberReservation.getStatus().equals(ReservationStatus.ATTENDED))
                        .toList()
                        .size()
        );
    }
}
