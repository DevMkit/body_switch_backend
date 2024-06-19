package kr.co.softhubglobal.dto.center;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import kr.co.softhubglobal.dto.PageableDTO;
import kr.co.softhubglobal.dto.branch.BranchDTO;
import kr.co.softhubglobal.entity.center.BusinessClassification;
import kr.co.softhubglobal.entity.center.CenterManagerStatus;
import kr.co.softhubglobal.entity.center.CenterType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class CenterDTO {

    @Data
    public static class CenterManagerCreateRequest {

        private CenterManagerCreateInfo managerInfo;
        private CenterCreateInfo centerInfo;
        private Long headCenterId;
    }

    @Data
    @EqualsAndHashCode(callSuper = false)
    public static class CenterManagerSearchRequest extends PageableDTO.Request {

        private Long centerId;
        private String name;
        private LocalDate registeredDateFrom;
        private LocalDate registeredDateTo;
        private CenterManagerStatus status;
    }

    @Data
    @EqualsAndHashCode(callSuper = false)
    public static class CenterSearchRequest extends PageableDTO.Request {

        private String branchName;
        private CenterType type;
    }

    @Data
    @AllArgsConstructor
    public static class CenterManagerCreateInfo {

        @NotNull(message = "center.manager.username.not.null")
        @NotEmpty(message = "center.manager.username.not.empty")
        private String username;

        @NotNull(message = "center.manager.name.not.null")
        @NotEmpty(message = "center.manager.name.not.empty")
        private String name;

        @NotNull(message = "center.manager.password.not.null")
        @NotEmpty(message = "center.manager.password.not.empty")
        private String password;

        @NotNull(message = "center.manager.phone.number.not.null")
        @NotEmpty(message = "center.manager.phone.number.not.empty")
        private String phoneNumber;

        @NotNull(message = "center.manager.email.not.null")
        @NotEmpty(message = "center.manager.email.not.empty")
        @Email(message = "center.manager.email.invalid.pattern")
        private String email;
    }

    @Data
    @AllArgsConstructor
    public static class CenterCreateInfo {

        @NotNull(message = "center.business.name.not.null")
        @NotEmpty(message = "center.business.name.not.empty")
        private String businessName;

        @NotNull(message = "center.business.number.not.null")
        @NotEmpty(message = "center.business.number.not.empty")
        private String businessNumber;

        @NotNull(message = "center.business.classification.not.null")
        private BusinessClassification businessClassification;

        @NotNull(message = "center.business.type.not.null")
        @NotEmpty(message = "center.business.type.not.empty")
        private String businessType;

        @NotNull(message = "center.postal.code.not.null")
        @NotEmpty(message = "center.postal.code.not.empty")
        private String postalCode;

        @NotNull(message = "center.city.not.null")
        @NotEmpty(message = "center.city.not.empty")
        private String city;

        @NotNull(message = "center.address.not.null")
        @NotEmpty(message = "center.address.not.empty")
        private String address;

        private String addressDetail;
        private String representativeNumber;

        @Email(message = "center.email.invalid.pattern")
        private String email;

        private String homepage;
    }

    @Data
    @AllArgsConstructor
    public static class CenterManagerInfo {

        private Long id;
        private String businessName;
        private String username;
        private String name;
        private String phoneNumber;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        private LocalDateTime registeredDate;
        private CenterManagerStatus status;
    }

    @Data
    @AllArgsConstructor
    public static class CenterManagerDetailInfo {

        private ManagerInfo manageInfo;
        private BranchInfo branchInfo;
    }

    @Data
    @AllArgsConstructor
    public static class ManagerInfo {

        private Long id;
        private String name;
        private String phoneNumber;
        private String username;
        private String email;
        private CenterManagerStatus status;
    }

    @Data
    @AllArgsConstructor
    public static class BranchInfo {

        private String businessName;
        private String businessNumber;
        private String representativeNumber;
        private String email;
        private BusinessClassification businessClassification;
        private String homepage;
        private String businessType;
        private String address;
        private String addressDetail;
    }

    @Data
    @AllArgsConstructor
    public static class CenterInfo {

        private Long centerId;
        private String branchName;
        private String representativeNumber;
        private String representativeName;
        private String representativePhoneNumber;
        private String representativeEmail;
        private CenterType type;
    }

    @Data
    @AllArgsConstructor
    public static class CenterDetailInfo {

        private CenterManagerDetailInfo centerManagerDetailInfo;
        private BranchDTO.BranchDetailInfo branchDetailInfo;
        private List<BranchDTO.BranchExerciseRoomInfo> branchExerciseRoomInfoList;
        private List<BranchProductCategoryInfo> branchProductCategoryList;
        private List<BranchProductInfo> branchProductList;
    }

    @Data
    @AllArgsConstructor
    public static class BranchProductCategoryInfo {

        private Long categoryId;
        private String categoryName;
    }

    @Data
    @AllArgsConstructor
    public static class BranchProductInfo {

        private Long productId;
        private Long categoryId;
        private String productName;
        private Integer price;
        private Integer quantity;
    }
}
