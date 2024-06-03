package kr.co.softhubglobal.dto.course;

import kr.co.softhubglobal.entity.branch.BranchFacility;
import kr.co.softhubglobal.entity.course.CourseTicket;
import kr.co.softhubglobal.entity.course.CourseTicketImage;
import kr.co.softhubglobal.entity.employee.EmployeeResponsibility;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class CourseTicketDetailInfoMapper implements Function<CourseTicket, CourseTicketDTO.CourseTicketDetailInfo> {

    @Override
    public CourseTicketDTO.CourseTicketDetailInfo apply(CourseTicket courseTicket) {
        return new CourseTicketDTO.CourseTicketDetailInfo(
                courseTicket.getId(),
                courseTicket.getClassType().toString(),
                courseTicket.getBranch().getBranchName(),
                courseTicket.getTicketName(),
                courseTicket.getRegularPrice(),
                courseTicket.getDiscountRate(),
                courseTicket.getFinalPrice(),
                courseTicket.getClassDetail(),
                courseTicket.getCourseTicketImages().stream().map(CourseTicketImage::getImageUrl).toList(),
                courseTicket.getCourseCurriculumList()
                        .stream()
                        .map(courseCurriculum -> new CourseTicketDTO.CourseCurriculumInfo(
                                courseCurriculum.getId(),
                                courseCurriculum.getTitle(),
                                courseCurriculum.getSummary(),
                                courseCurriculum.getImageUrl()
                        ))
                        .toList(),
                new CourseTicketDTO.BranchInfo(
                        courseTicket.getBranch().getId(),
                        courseTicket.getBranch().getBranchName(),
                        courseTicket.getBranch().getBranchDescription(),
                        courseTicket.getBranch().getBranchWorkHoursList()
                                .stream()
                                .map(branchWorkHours -> new CourseTicketDTO.BranchWorkHoursInfo(
                                        branchWorkHours.getDayOfWeek().toString(),
                                        branchWorkHours.getIsOpen(),
                                        branchWorkHours.getOpenTime(),
                                        branchWorkHours.getCloseTime()
                                ))
                                .toList(),
                        courseTicket.getBranch().getBranchFacilities().stream().map(BranchFacility::getName).toList(),
                        courseTicket.getBranch().getReservationPolicy(),
                        courseTicket.getBranch().getBranchDetailDescription()
                ),
                courseTicket.getCourseTrainers()
                        .stream()
                        .map(courseTrainer -> new CourseTicketDTO.CourseTrainerInfo(
                                courseTrainer.getId(),
                                courseTrainer.getEmployee().getUser().getName(),
                                courseTrainer.getEmployee().getProfileImage(),
                                courseTrainer.getEmployee().getResponsibilities().stream().map(EmployeeResponsibility::getName).toList(),
                                courseTrainer.getEmployee().getIntroduction()
                        ))
                        .toList()
        );
    }
}
