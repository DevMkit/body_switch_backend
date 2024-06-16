package kr.co.softhubglobal.dto.course;

import lombok.AllArgsConstructor;
import lombok.Data;

public class CourseClassDTO {

    @Data
    @AllArgsConstructor
    public static class CourseClassInfo {

        private Long id;
        private String className;
        private String exerciseRoom;
        private String trainerName;
        private Integer currentMemberCount;
        private Integer maxMemberCount;
    }
}
