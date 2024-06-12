package kr.co.softhubglobal.dto.member;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import kr.co.softhubglobal.entity.member.Gender;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

public class MemberDTO {

    @Data
    public static class MemberCreateRequest {

        @NotNull(message = "member.username.not.null")
        @NotEmpty(message = "member.username.not.empty")
        private String username;

        @NotNull(message = "member.password.not.null")
        @NotEmpty(message = "member.password.not.empty")
        private String password;

        @NotNull(message = "member.name.not.null")
        @NotEmpty(message = "member.name.not.empty")
        private String name;

        @NotNull(message = "member.phone.number.not.null")
        @NotEmpty(message = "member.phone.number.not.empty")
        private String phoneNumber;

        @NotNull(message = "member.birthdate.not.null")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        private LocalDate birthDate;

        @NotNull(message = "member.gender.not.null")
        private Gender gender;

        @NotNull(message = "member.email.not.null")
        @NotEmpty(message = "member.email.not.empty")
        @Email(message = "member.email.invalid.pattern")
        private String email;

        private MultipartFile profileImage;
        private String postalCode;
        private String address;
        private String addressDetail;
    }

    @Data
    public static class MemberUsernameCheckRequest {

        @NotNull(message = "member.username.not.null")
        @NotEmpty(message = "member.username.not.empty")
        private String username;
    }

    @Data
    @AllArgsConstructor
    public static class MemberInfo {

        private String username;
        private String name;
        private String gender;
    }
}
