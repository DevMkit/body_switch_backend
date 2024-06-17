package kr.co.softhubglobal.dto.course;

import kr.co.softhubglobal.entity.member.MemberCourseTicket;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class ActiveCourseTicketInfoMapper implements Function<MemberCourseTicket, CourseTicketDTO.ActiveCourseTicketInfo> {

    @Override
    public CourseTicketDTO.ActiveCourseTicketInfo apply(MemberCourseTicket memberCourseTicket) {
        return new CourseTicketDTO.ActiveCourseTicketInfo(
                memberCourseTicket.getCourseTicket().getId(),
                memberCourseTicket.getCourseTicket().getClassType(),
                memberCourseTicket.getCourseTicket().getBranch().getBranchName(),
                memberCourseTicket.getCourseTicket().getTicketName(),
                memberCourseTicket.getStartDate(),
                memberCourseTicket.getStartDate().plusMonths(memberCourseTicket.getCourseTicket().usagePeriod),
                memberCourseTicket.getUsedCount(),
                memberCourseTicket.getCourseTicket().usageCount
        );
    }
}
