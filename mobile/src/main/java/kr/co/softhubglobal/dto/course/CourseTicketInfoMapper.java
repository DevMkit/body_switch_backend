package kr.co.softhubglobal.dto.course;

import kr.co.softhubglobal.entity.course.CourseTicket;
import kr.co.softhubglobal.entity.course.CourseTicketImage;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class CourseTicketInfoMapper implements Function<CourseTicket, CourseTicketDTO.CourseTicketInfo> {

    @Override
    public CourseTicketDTO.CourseTicketInfo apply(CourseTicket courseTicket) {
        return new CourseTicketDTO.CourseTicketInfo(
                courseTicket.getId(),
                courseTicket.getClassType(),
                courseTicket.getTicketName(),
                courseTicket.getCourseTicketImages().stream().findAny().map(CourseTicketImage::getImageUrl).orElse(null),
                courseTicket.getFinalPrice(),
                courseTicket.getUsageCount(),
                courseTicket.getUsagePeriod()
        );
    }
}
