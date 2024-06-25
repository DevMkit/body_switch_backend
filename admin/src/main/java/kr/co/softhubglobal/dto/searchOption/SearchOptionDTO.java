package kr.co.softhubglobal.dto.searchOption;

import kr.co.softhubglobal.entity.center.Center;
import kr.co.softhubglobal.entity.course.CourseTicket;
import kr.co.softhubglobal.entity.employee.Employee;
import lombok.Data;

public class SearchOptionDTO {

    @Data
    public static class HeadBranchInfo {

        private Long headBranchId;
        private String headBranchName;

        public HeadBranchInfo(final Center center) {
            this.headBranchId = center.getId();
            this.headBranchName = center.getBranch().getBranchName();
        }
    }

    @Data
    public static class BranchInfo {

        private Long branchId;
        private String branchName;

        public BranchInfo(final Center center) {
            this.branchId = center.getBranch().getId();
            this.branchName = center.getBranch().getBranchName();
        }
    }

    @Data
    public static class CourseTicketInfo {

        private Long courseTicketId;
        private String courseTicketName;

        public CourseTicketInfo(final CourseTicket courseTicket) {
            this.courseTicketId = courseTicket.getId();
            this.courseTicketName = courseTicket.getTicketName();
        }
    }

    @Data
    public static class CourseTicketTrainerInfo {

        private Long trainerId;
        private String trainerName;

        public CourseTicketTrainerInfo(final Employee employee) {
            this.trainerId = employee.getId();
            this.trainerName = employee.getUser().getName();
        }
    }
}
