package kr.co.softhubglobal.dto.employee;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import kr.co.softhubglobal.dto.member.MemberDTO;
import kr.co.softhubglobal.entity.employee.EmployeeClassification;
import kr.co.softhubglobal.entity.employee.Responsibilities;
import kr.co.softhubglobal.entity.member.Gender;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

public class EmployeeDTO {

    @Data
    public static class EmployeeCreateRequest {

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
    @AllArgsConstructor
    public static class EmployeeInfo {

        private String name;
        private String gender;
        private String phoneNumber;
        private List<String> responsibilities;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        private LocalDate hiredDate;
        private String classification;
    }
}
