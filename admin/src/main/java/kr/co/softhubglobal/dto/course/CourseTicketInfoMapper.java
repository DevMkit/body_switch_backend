package kr.co.softhubglobal.dto.course;

import kr.co.softhubglobal.entity.course.CourseTicket;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class CourseTicketInfoMapper implements Function<CourseTicket, CourseTicketDTO.CourseTicketInfo> {

    @Override
    public CourseTicketDTO.CourseTicketInfo apply(CourseTicket courseTicket) {
        return new CourseTicketDTO.CourseTicketInfo(
                courseTicket.getBranch().getBranchName(),
                courseTicket.getClassType().toString(),
                courseTicket.getTicketName(),
                courseTicket.getUsagePeriod(),
                courseTicket.getUsageCount(),
                courseTicket.getFinalPrice(),
                0,
                courseTicket.getSaleStatus().toString()
        );
    }
}
