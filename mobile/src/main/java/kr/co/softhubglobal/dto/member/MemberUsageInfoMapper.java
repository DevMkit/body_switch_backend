package kr.co.softhubglobal.dto.member;

import kr.co.softhubglobal.entity.member.Member;
import kr.co.softhubglobal.entity.member.MemberCourseTicketStatus;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class MemberUsageInfoMapper implements Function<Member, MemberDTO.MemberUsageInfo> {

    @Override
    public MemberDTO.MemberUsageInfo apply(Member member) {
        return new MemberDTO.MemberUsageInfo(
                0,
                member.getCourseTickets()
                        .stream()
                        .filter(memberCourseTicket -> memberCourseTicket.getStatus().equals(MemberCourseTicketStatus.ACTIVE))
                        .toList()
                        .size(),
                0
        );
    }
}
