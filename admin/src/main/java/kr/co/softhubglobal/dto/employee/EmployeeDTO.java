package kr.co.softhubglobal.dto.employee;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import kr.co.softhubglobal.dto.PageableDTO;
import kr.co.softhubglobal.dto.member.MemberDTO;
import kr.co.softhubglobal.entity.center.CenterManagerStatus;
import kr.co.softhubglobal.entity.employee.EmployeeClassification;
import kr.co.softhubglobal.entity.employee.EmployeeResponsibility;
import kr.co.softhubglobal.entity.employee.Responsibilities;
import kr.co.softhubglobal.entity.member.Gender;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

public class EmployeeDTO {

    @Data
    public static class EmployeeCreateRequest {

        private MultipartFile profileImage;

        @NotNull(message = "employee.name.not.null")
        @NotEmpty(message = "employee.name.not.empty")
        private String name;

        @NotNull(message = "employee.branch.id.not.null")
        private Long branchId;

        @NotNull(message = "employee.username.not.null")
        @NotEmpty(message = "employee.username.not.empty")
        private String username;

        @NotNull(message = "employee.phone.number.not.null")
        @NotEmpty(message = "employee.phone.number.not.empty")
        private String phoneNumber;

        @NotNull(message = "employee.password.not.null")
        @NotEmpty(message = "employee.password.not.empty")
        private String password;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        private LocalDate birthDate;

        @Email(message = "employee.email.invalid.pattern")
        private String email;

        @NotNull(message = "employee.gender.not.null")
        private Gender gender;

        private String introduction;

        private List<Responsibilities> responsibilities;

        @NotNull(message = "employee.classification.not.null")
        private EmployeeClassification employeeClassification;

        @NotNull(message = "employee.hired.date.not.null")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        private LocalDate hiredDate;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        private LocalDate leftDate;
    }

    @Data
    public static class EmployeeUpdateRequest {

        private MultipartFile profileImage;
        private String name;
        private String phoneNumber;
        private String password;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        private LocalDate birthDate;
        @Email(message = "employee.email.invalid.pattern")
        private String email;
        private Gender gender;
        private String introduction;
        private List<Responsibilities> responsibilities;
        private EmployeeClassification employeeClassification;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        private LocalDate hiredDate;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        private LocalDate leftDate;
    }

    @Data
    @EqualsAndHashCode(callSuper = false)
    public static class EmployeeSearchRequest extends PageableDTO.Request {

        private Long centerId;
        private String name;
        private List<Responsibilities> responsibilities;
        private List<EmployeeClassification> classifications;
    }

    @Data
    @AllArgsConstructor
    public static class EmployeeInfo {

        private Long id;
        private String name;
        private String gender;
        private String phoneNumber;
        private List<Responsibilities> responsibilities;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        private LocalDate hiredDate;
        private EmployeeClassification classification;
    }

    @Data
    @AllArgsConstructor
    public static class EmployeeDetailInfo {

        private Long id;
        private String profileImage;
        private String username;
        private String name;
        private String branchName;
        private String phoneNumber;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        private LocalDate birthDate;
        private Gender gender;
        private String email;
        private String introduction;
        private List<Responsibilities> responsibilities;
        private EmployeeClassification classification;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        private LocalDate hiredDate;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        private LocalDate leftDate;
    }
}
