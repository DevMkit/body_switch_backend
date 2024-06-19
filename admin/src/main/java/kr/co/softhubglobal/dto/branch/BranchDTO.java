package kr.co.softhubglobal.dto.branch;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

public class BranchDTO {

    @Data
    public static class BranchCreateRequest {

        @NotNull(message = "branch.center.id.not.null")
        private Long centerId;

        @NotNull(message = "branch.name.not.null")
        @NotEmpty(message = "branch.name.not.empty")
        private String branchName;

        private String branchDescription;
        private boolean isAddressSame;
        private String postalCode;
        private String city;
        private String address;
        private String addressDetail;
        private List<WorkHoursInfo> workHoursInfoList;
        private String reservationPolicy;
        private List<FacilityInfo> facilityInfoList;
        private String branchDetailDescription;
    }

    @Data
    public static class BranchUpdateRequest {

        private String branchName;
        private String branchDescription;
        private boolean isAddressSame;
        private String postalCode;
        private String city;
        private String address;
        private String addressDetail;
        private List<WorkHoursInfo> workHoursInfoList;
        private String reservationPolicy;
        private List<FacilityInfo> facilityInfoList;
        private String branchDetailDescription;
    }

    @Data
    public static class BranchExerciseRoomCreateRequest {

        @NotNull(message = "branch.room.name.not.null")
        @NotEmpty(message = "branch.room.name.not.empty")
        private String roomName;
    }

    @Data
    @AllArgsConstructor
    public static class WorkHoursInfo {

        private boolean isOpen;
        private DayOfWeek dayOfWeek;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
        private LocalTime openTime;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
        private LocalTime closeTime;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class FacilityInfo {

        private String facilityName;
    }

    @Data
    @AllArgsConstructor
    public static class BranchInfo {

        private Long branchId;
        private String branchName;
        private String centerName;
    }

    @Data
    @AllArgsConstructor
    public static class BranchDetailInfo {

        private Long branchId;
        private String branchName;
        private String branchDescription;
        private String address;
        private List<WorkHoursInfo> workHoursInfoList;
        private String reservationPolicy;
        private List<FacilityInfo> facilityInfoList;
        private String branchDetailDescription;
    }

    @Data
    @AllArgsConstructor
    public static class BranchExerciseRoomInfo {

        private Long roomId;
        private String name;
    }
}
