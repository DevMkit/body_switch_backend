package kr.co.softhubglobal.dto.member;

import kr.co.softhubglobal.entity.common.BaseDateEntity;
import kr.co.softhubglobal.entity.member.Member;
import kr.co.softhubglobal.entity.member.MemberOrder;
import kr.co.softhubglobal.entity.member.MemberReservation;
import kr.co.softhubglobal.entity.member.ReservationStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class MemberInfoMapper implements Function<Member, MemberDTO.MemberInfo> {

    private final MemberTicketInfoMapper memberTicketInfoMapper;

    @Override
    public MemberDTO.MemberInfo apply(Member member) {

        List<MemberOrder> orders = member.getOrders()
                .stream()
                .filter(memberOrder -> memberOrder.getPaymentStatus().equals("PAID"))
                .sorted(Comparator.comparing(BaseDateEntity::getRegisteredDate))
                .toList();

        List<MemberReservation> reservations = member.getReservations()
                .stream()
                .filter(memberReservation -> memberReservation.getStatus().equals(ReservationStatus.ATTENDED))
                .sorted(Comparator.comparing(BaseDateEntity::getRegisteredDate, Comparator.reverseOrder()))
                .toList();

        return new MemberDTO.MemberInfo(
                member.getId(),
                member.getUser().getUsername(),
                member.getUser().getName(),
                member.getGender(),
                member.getAge(),
                member.getUser().getPhoneNumber(),
                member.getCourseTickets().stream()
                        .map(memberTicketInfoMapper)
                        .toList(),
                orders.isEmpty() ? null : orders.stream().findFirst().get().getPaidAt(),
                reservations.isEmpty() ? null : reservations.stream().findFirst().get().getReservationDate(),
                member.getRegisteredDate(),
                member.isSMSReceive()
        );
    }
}
