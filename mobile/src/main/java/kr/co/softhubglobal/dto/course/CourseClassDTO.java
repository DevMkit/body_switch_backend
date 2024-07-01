package kr.co.softhubglobal.dto.course;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import kr.co.softhubglobal.dto.member.MemberDTO;
import kr.co.softhubglobal.entity.course.CourseClassType;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

public class CourseClassDTO {

    @Data
    public static class CourseClassSearchRequest {

        private Long branchId;
        private CourseClassType type;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        private LocalDate classDate;
    }

    @Data
    public static class TrainerCourseClassSearchRequest {

        private Long branchId;
        private CourseClassType type;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        private LocalDate classDate;
    }

    @Data
    public static class CourseClassTimeReserveRequest {

        @NotNull(message = "course.class.time.id.not.null")
        private Long courseClassTimeId;

        @NotNull(message = "course.class.time.reservation.date.not.null")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        private LocalDate reservationDate;
    }

    @Data
    @AllArgsConstructor
    public static class CourseClassInfo {

        private Long id;
        private String className;
        private String exerciseRoom;
        private String trainerName;
        private Integer currentMemberCount;
        private Integer maxMemberCount;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
        private LocalTime startTime;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
        private LocalTime endTime;
    }

    @Data
    @AllArgsConstructor
    public static class CourseClassTimeInfo {

        private Long id;
        private String className;
        private String exerciseRoom;
        private String trainerName;
        private Integer currentMemberCount;
        private Integer maxMemberCount;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
        private LocalTime startTime;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
        private LocalTime endTime;
    }

    @Data
    @AllArgsConstructor
    public static class CourseClassTimeDetailInfo {

        private Long id;
        private CourseClassType type;
        private String branchName;
        private String className;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
        private LocalTime startTime;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
        private LocalTime endTime;
        private String exerciseRoom;
        private String trainerName;
        private MemberDTO.MemberCourseTicketInfo activeCourseTicketInfo;
    }
}
