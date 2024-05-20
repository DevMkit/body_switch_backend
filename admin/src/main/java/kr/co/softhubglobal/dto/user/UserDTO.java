package kr.co.softhubglobal.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

public class UserDTO {

    @Data
    public static class UserCreateRequest {

        @NotNull(message = "user.username.not.null")
        @NotEmpty(message = "user.username.not.empty")
        private String username;

        @NotNull(message = "user.name.not.null")
        @NotEmpty(message = "user.name.not.empty")
        private String name;

        @NotNull(message = "user.password.not.null")
        @NotEmpty(message = "user.password.not.empty")
        private String password;

        @NotNull(message = "user.phone.number.not.null")
        @NotEmpty(message = "user.phone.number.not.empty")
        private String phoneNumber;

        @NotNull(message = "user.email.not.null")
        @NotEmpty(message = "user.email.not.empty")
        @Email(message = "user.email.invalid.pattern")
        private String email;
    }

    @Data
    @AllArgsConstructor
    public static class UserInfo {

        private String username;
        private String name;
        private String phoneNumber;
        private String email;
        private String userType;
    }
}
