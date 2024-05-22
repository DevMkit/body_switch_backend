package kr.co.softhubglobal.dto.store;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import kr.co.softhubglobal.entity.store.BusinessClassification;
import kr.co.softhubglobal.entity.store.StoreStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class StoreDTO {

    @Data
    public static class StoreRepresentativeCreateRequest {

        private StoreRepresentativeCreateInfo representativeInfo;
        private StoreCreateInfo storeInfo;
    }

    @Data
    @AllArgsConstructor
    public static class StoreRepresentativeCreateInfo {

        @NotNull(message = "store.representative.username.not.null")
        @NotEmpty(message = "store.representative.username.not.empty")
        private String username;

        @NotNull(message = "store.representative.name.not.null")
        @NotEmpty(message = "store.representative.name.not.empty")
        private String name;

        @NotNull(message = "store.representative.password.not.null")
        @NotEmpty(message = "store.representative.password.not.empty")
        private String password;

        @NotNull(message = "store.representative.phone.number.not.null")
        @NotEmpty(message = "store.representative.phone.number.not.empty")
        private String phoneNumber;

        @NotNull(message = "store.representative.email.not.null")
        @NotEmpty(message = "store.representative.email.not.empty")
        @Email(message = "store.representative.email.invalid.pattern")
        private String email;
    }

    @Data
    @AllArgsConstructor
    public static class StoreCreateInfo {

        @NotNull(message = "store.business.name.not.null")
        @NotEmpty(message = "store.business.name.not.empty")
        public String businessName;

        @NotNull(message = "store.business.number.not.null")
        @NotEmpty(message = "store.business.number.not.empty")
        public String businessNumber;

        @NotNull(message = "store.business.classification.not.null")
        public BusinessClassification businessClassification;

        @NotNull(message = "store.business.type.not.null")
        @NotEmpty(message = "store.business.type.not.empty")
        public String businessType;

        @NotNull(message = "store.postal.code.not.null")
        @NotEmpty(message = "store.postal.code.not.empty")
        private String postalCode;

        @NotNull(message = "store.city.not.null")
        @NotEmpty(message = "store.city.not.empty")
        private String city;

        @NotNull(message = "store.address.not.null")
        @NotEmpty(message = "store.address.not.empty")
        private String address;

        private String addressDetail;
        private String representativeNumber;

        @Email(message = "store.email.invalid.pattern")
        private String email;

        private String homepage;
        private StoreStatus status;
    }

    @Data
    @AllArgsConstructor
    public static class StoreRepresentativeInfo {

        public String businessName;
        public String username;
        public String name;
        public String phoneNumber;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        private LocalDateTime registeredDate;
        private StoreStatus status;
    }
}
