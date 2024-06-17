package kr.co.softhubglobal.dto.course;

import com.fasterxml.jackson.annotation.JsonFormat;
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
}
