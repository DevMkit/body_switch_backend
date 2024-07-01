package kr.co.softhubglobal.dto.member;

import kr.co.softhubglobal.entity.member.MemberCourseTicket;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class MemberCourseTicketInfoMapper implements Function<MemberCourseTicket, MemberDTO.MemberCourseTicketInfo> {

    @Override
    public MemberDTO.MemberCourseTicketInfo apply(MemberCourseTicket memberCourseTicket) {
        return new MemberDTO.MemberCourseTicketInfo(
                memberCourseTicket.getCourseTicket().getId(),
                memberCourseTicket.getCourseTicket().getClassType(),
                memberCourseTicket.getCourseTicket().getBranch().getBranchName(),
                memberCourseTicket.getCourseTicket().getTicketName(),
                memberCourseTicket.getStartDate(),
                memberCourseTicket.getStartDate().plusMonths(memberCourseTicket.getCourseTicket().usagePeriod),
                memberCourseTicket.getUsedCount(),
                memberCourseTicket.getCourseTicket().usageCount,
                memberCourseTicket.getStatus()
        );
    }
}
