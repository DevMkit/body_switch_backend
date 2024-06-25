package kr.co.softhubglobal.dto.member;

import ch.qos.logback.core.joran.action.AppenderRefAction;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.sun.jdi.ClassType;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import kr.co.softhubglobal.dto.PageableDTO;
import kr.co.softhubglobal.entity.center.CenterManagerStatus;
import kr.co.softhubglobal.entity.course.CourseClassType;
import kr.co.softhubglobal.entity.member.Gender;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

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

        private String postalCode;
        private String address;
        private String addressDetail;
    }

    @Data
    @EqualsAndHashCode(callSuper = false)
    public static class MemberSearchRequest extends PageableDTO.Request {

        private Long branchId;
        private String searchInput;
        private List<CourseClassType> classTypes;
        private Long courseTicketId;
        private Long courseTrainerId;
        private List<Gender> genders;
    }

    @Data
    @AllArgsConstructor
    public static class MemberInfo {

        private String memberId;
        private String username;
        private String name;
        private Gender gender;
        private int age;
        private String phoneNumber;
        private List<MemberTicketInfo> activeTickets;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        private LocalDateTime firstPaymentDate;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        private LocalDate lastAttendanceDate;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        private LocalDateTime lockerExpireDate;
        private boolean isSMSReceive;
    }

    @Data
    @AllArgsConstructor
    public static class MemberTicketInfo {

        private Long ticketId;
        private String ticketName;
        private Integer usageCount;
        private Integer usagePeriod;
        private String representativeTeacher;
        private Integer remainingCount;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        private LocalDate expireDate;
        private Integer attendanceCount;
    }
}
