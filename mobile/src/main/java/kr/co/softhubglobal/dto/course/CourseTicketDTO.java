package kr.co.softhubglobal.dto.course;

import com.fasterxml.jackson.annotation.JsonFormat;
import kr.co.softhubglobal.entity.course.CourseClassType;
import kr.co.softhubglobal.entity.employee.Responsibilities;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class CourseTicketDTO {

    @Data
    public static class CourseTicketSearchRequest {

        private Long branchId;
    }

    @Data
    @AllArgsConstructor
    public static class CourseTicketInfo {

        private Long id;
        private CourseClassType classType;
        private String ticketName;
        private String image;
        private double finalPrice;
        private Integer usageCount;
        private Integer usagePeriod;
    }

    @Data
    @AllArgsConstructor
    public static class CourseTicketDetailInfo {

        private Long id;
        private boolean isSaved;
        private CourseClassType classType;
        private String branchName;
        private String ticketName;
        private double regularPrice;
        private double discountRate;
        private double finalPrice;
        private String classDetail;
        private Integer usageCount;
        private Integer usagePeriod;
        private List<String> images;
        private List<CourseCurriculumInfo> curriculumInfoList;
        private CourseTicketDTO.BranchInfo branchInfo;
        private List<CourseTrainerInfo> trainerInfoList;
    }

    @Data
    @AllArgsConstructor
    public static class CourseCurriculumInfo {

        private Long id;
        private String title;
        private String summary;
        private String imageUrl;
    }

    @Data
    @AllArgsConstructor
    public static class BranchInfo {

        private Long id;
        private String branchName;
        private String branchDescription;
        private String address;
        private String addressDetail;
        private List<BranchWorkHoursInfo> workHours;
        private String reservationPolicy;
        private List<String> facilities;
        private String phoneNumber;
        private String branchDetailDescription;
    }

    @Data
    @AllArgsConstructor
    public static class BranchWorkHoursInfo {

        private String dayOfWeek;
        private boolean isOpen;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
        private LocalTime openTime;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
        private LocalTime closeTime;
    }

    @Data
    @AllArgsConstructor
    public static class CourseTrainerInfo {

        private Long id;
        private String name;
        private String image;
        private List<Responsibilities> responsibilities;
        private String introduction;
    }
}
