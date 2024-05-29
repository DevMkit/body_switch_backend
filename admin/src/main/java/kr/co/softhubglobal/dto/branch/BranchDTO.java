package kr.co.softhubglobal.dto.branch;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

public class BranchDTO {

    @Data
    public static class MainBranchCreateRequest {

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
        private List<FacilityInfo> facilityInfoList;
    }

    @Data
    public static class BranchExerciseRoomCreateRequest {

        @NotNull(message = "branch.room.name.not.null")
        @NotEmpty(message = "branch.room.name.not.empty")
        private String roomName;
    }

    @Data
    public static class WorkHoursInfo {

        private boolean isOpen;
        private DayOfWeek dayOfWeek;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
        private LocalTime openTime;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
        private LocalTime closeTime;
    }

    @Data
    public static class FacilityInfo {

        private String name;
    }
}
