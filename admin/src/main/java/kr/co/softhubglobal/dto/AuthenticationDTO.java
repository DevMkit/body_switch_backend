package kr.co.softhubglobal.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import kr.co.softhubglobal.entity.center.CenterType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class AuthenticationDTO {

    @Data
    public static class AuthenticationRequest {

        @NotNull(message = "authentication.request.username.not.null")
        @NotEmpty(message = "authentication.request.username.not.empty")
        private String username;

        @NotNull(message = "authentication.request.username.not.null")
        @NotEmpty(message = "authentication.request.username.not.empty")
        private String password;
    }


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AuthenticationResponse {

        private String token;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime expiresAt;
        private String userRole;
        private CenterType centerType;
    }
}