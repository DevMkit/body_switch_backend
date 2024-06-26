package kr.co.softhubglobal.dto.member;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import kr.co.softhubglobal.dto.course.CourseTicketDTO;
import kr.co.softhubglobal.entity.course.CourseClassType;
import kr.co.softhubglobal.entity.member.Gender;
import kr.co.softhubglobal.entity.member.MemberCourseTicketStatus;
import kr.co.softhubglobal.entity.member.ReservationStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalTime;
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

        private MultipartFile profileImage;
        private String postalCode;
        private String address;
        private String addressDetail;
    }

    @Data
    public static class MemberUpdateRequest {

        private MultipartFile profileImage;
        private String password;
        private String phoneNumber;
        private Gender gender;
        @Email(message = "member.email.invalid.pattern")
        private String email;
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
    public static class MemberReservationSearchRequest {

        private ReservationStatus reservationStatus;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        private LocalDate fromDate;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        private LocalDate endDate;
    }

    @Data
    public static class MemberReservationCancelRequest {

        @NotNull(message = "member.reservation.id.not.null")
        private Long reservationId;
    }

    @Data
    @AllArgsConstructor
    public static class MemberInfo {

        private String memberId;
        private String name;
        private String profileImage;
    }

    @Data
    @AllArgsConstructor
    public static class MemberDetailInfo {

        private String memberId;
        private String profileImage;
        private String username;
        private String name;
        private String phoneNumber;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        private LocalDate birthDate;
        private Gender gender;
        private String email;
        private String postalCode;
        private String address;
        private String addressDetail;
    }

    @Data
    @AllArgsConstructor
    public static class MemberUsageInfo {
        private Integer reservationCount;
        private Integer ticketCount;
        private Integer messageCount;
    }

    @Data
    @AllArgsConstructor
    public static class MemberReservationInfo {

        private Long reservationId;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        private LocalDate reservationDate;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
        private LocalTime startTime;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
        private LocalTime endTime;
        private String className;
        private String exerciseRoom;
        private String trainerName;
        private CourseClassType courseClassType;
        private String branchName;
        private ReservationStatus status;
    }

    @Data
    @AllArgsConstructor
    public static class MemberCourseTicketInfo {

        private Long courseTicketId;
        private CourseClassType classType;
        private String branchName;
        private String ticketName;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        private LocalDate issueDate;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        private LocalDate expireDate;
        private Integer usedCount;
        private Integer usageCount;
        private MemberCourseTicketStatus status;
    }

    @Data
    @AllArgsConstructor
    public static class MemberReservationDetailInfo {

        private Long reservationId;
        private CourseClassType courseClassType;
        private String branchName;
        private String className;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        private LocalDate reservationDate;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
        private LocalTime startTime;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
        private LocalTime endTime;
        private String exerciseRoom;
        private String trainerName;
        private MemberCourseTicketInfo activeCourseTicketInfo;
    }

    @Data
    @AllArgsConstructor
    public static class MemberCertificateInfo {

        private String memberId;
        private String name;
        private List<MemberCourseTicketInfo> activeTickets;
    }

    @Data
    @AllArgsConstructor
    public static class MemberTicketInfo {

        private List<String> vodTickets;
        private List<MemberCourseTicketInfo> activeTickets;
        private List<MemberCourseTicketInfo> expiredTickets;
    }
}
