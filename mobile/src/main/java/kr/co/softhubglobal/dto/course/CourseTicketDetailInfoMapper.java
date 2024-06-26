package kr.co.softhubglobal.dto.course;

import kr.co.softhubglobal.entity.branch.BranchFacility;
import kr.co.softhubglobal.entity.course.CourseTicket;
import kr.co.softhubglobal.entity.course.CourseTicketImage;
import kr.co.softhubglobal.entity.employee.EmployeeResponsibility;
import kr.co.softhubglobal.entity.member.Member;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class CourseTicketDetailInfoMapper implements Function<CourseTicket, CourseTicketDTO.CourseTicketDetailInfo> {

    @Override
    public CourseTicketDTO.CourseTicketDetailInfo apply(CourseTicket courseTicket) {
        return new CourseTicketDTO.CourseTicketDetailInfo(
                courseTicket.getId(),
                false,
                courseTicket.getClassType(),
                courseTicket.getBranch().getBranchName(),
                courseTicket.getTicketName(),
                courseTicket.getRegularPrice(),
                courseTicket.getDiscountRate(),
                courseTicket.getFinalPrice(),
                courseTicket.getClassDetail(),
                courseTicket.getUsageCount(),
                courseTicket.getUsagePeriod(),
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
                        courseTicket.getBranch().getAddress(),
                        courseTicket.getBranch().getAddressDetail(),
                        courseTicket.getBranch().getBranchWorkHoursList()
                                .stream()
                                .map(branchWorkHours -> new CourseTicketDTO.BranchWorkHoursInfo(
                                        branchWorkHours.getDayOfWeek().toString(),
                                        branchWorkHours.getIsOpen(),
                                        branchWorkHours.getOpenTime(),
                                        branchWorkHours.getCloseTime()
                                ))
                                .toList(),
                        courseTicket.getBranch().getReservationPolicy(),
                        courseTicket.getBranch().getBranchFacilities().stream().map(BranchFacility::getName).toList(),
                        "phoneNumber",
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

    public CourseTicketDTO.CourseTicketDetailInfo apply(CourseTicket courseTicket, Member member) {
        return new CourseTicketDTO.CourseTicketDetailInfo(
                courseTicket.getId(),
                member.getSavedCourseTickets().stream().anyMatch(memberSavedCourseTicket -> memberSavedCourseTicket.getCourseTicket().equals(courseTicket)),
                courseTicket.getClassType(),
                courseTicket.getBranch().getBranchName(),
                courseTicket.getTicketName(),
                courseTicket.getRegularPrice(),
                courseTicket.getDiscountRate(),
                courseTicket.getFinalPrice(),
                courseTicket.getClassDetail(),
                courseTicket.getUsageCount(),
                courseTicket.getUsagePeriod(),
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
                        courseTicket.getBranch().getAddress(),
                        courseTicket.getBranch().getAddressDetail(),
                        courseTicket.getBranch().getBranchWorkHoursList()
                                .stream()
                                .map(branchWorkHours -> new CourseTicketDTO.BranchWorkHoursInfo(
                                        branchWorkHours.getDayOfWeek().toString(),
                                        branchWorkHours.getIsOpen(),
                                        branchWorkHours.getOpenTime(),
                                        branchWorkHours.getCloseTime()
                                ))
                                .toList(),
                        courseTicket.getBranch().getReservationPolicy(),
                        courseTicket.getBranch().getBranchFacilities().stream().map(BranchFacility::getName).toList(),
                        "phoneNumber",
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
