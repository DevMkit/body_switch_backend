package kr.co.softhubglobal.dto.course;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import kr.co.softhubglobal.entity.course.CourseClassType;
import lombok.Data;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class CourseClassDTO {

    @Data
    public static class CourseClassCreateRequest {

        @NotNull(message = "course.class.branch.id.not.null")
        private Long branchId;

        @NotNull(message = "course.class.type.not.null")
        private CourseClassType courseClassType;

        @NotNull(message = "course.class.name.not.null")
        @NotEmpty(message = "course.class.name.not.empty")
        private String className;

        @NotNull(message = "course.class.exercise.room.id.not.null")
        public Long exerciseRoomId;

        @NotNull(message = "course.class.registration.start.date.not.null")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        private LocalDate registrationStartDate;

        @NotNull(message = "course.class.registration.end.date.not.null")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        private LocalDate registrationEndDate;

        private List<CourseClassTimeCreateInfo> classTimeInfoList;

        @NotNull(message = "course.class.trainer.id.not.null")
        private Long trainerId;

        @NotNull(message = "course.ticket.id.not.null")
        private Long ticketId;

        private Integer membersMaxCount;
        private String memberId;
        private String memo;
    }

    @Data
    public static class CourseClassTimeCreateInfo {

        @NotNull(message = "course.class.time.day.week.not.null")
        private DayOfWeek dayOfWeek;

        @NotNull(message = "course.class.time.start.time.not.null")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
        private LocalTime startTime;

        @NotNull(message = "course.class.time.end.time.not.null")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
        private LocalTime endTime;
    }
}
