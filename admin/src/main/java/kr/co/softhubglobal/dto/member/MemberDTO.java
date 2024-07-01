package kr.co.softhubglobal.dto.member;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import kr.co.softhubglobal.dto.PageableDTO;
import kr.co.softhubglobal.entity.branch.Branch;
import kr.co.softhubglobal.entity.course.CourseClassType;
import kr.co.softhubglobal.entity.employee.Employee;
import kr.co.softhubglobal.entity.member.Gender;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

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

        private Long headBranchId;
        private Long branchId;
        private String searchInput;
        private List<MemberType> memberTypes;
        private List<CourseClassType> classTypes;
        private Long courseTicketId;
        private Long courseTrainerId;
        private List<Gender> genders;
        private List<AgeRange> ageRanges;
        private List<Boolean> smsRecieveList;
        private List<RemainingCount> remainingCounts;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        private LocalDate ticketExpireDateFrom;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        private LocalDate ticketExpireDateTo;
    }

    public enum MemberType {
        ACTIVE, STOPPED, EXPIRED, WITHDREW
    }

    @Getter
    public enum AgeRange {
        UNDER_20(0, 19),
        AGE_20_30(20, 30),
        AGE_40_50(40, 50),
        AGE_50_60(50, 60),
        OVER_60(60, Integer.MAX_VALUE);

        private final int min;
        private final int max;

        AgeRange(int min, int max) {
            this.min = min;
            this.max = max;
        }
    }

    @Getter
    public enum RemainingCount {

        UNDER_3(3),
        UNDER_5(5),
        UNDER_10(10);

        private final int value;

        RemainingCount(int value) {
            this.value = value;
        }
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
    public static class MemberDetailInfo {

        private String memberId;
        private String profileImage;
        private MemberType type;
        private String name;
        private String username;
        private String phoneNumber;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        private LocalDate birthDate;
        private int age;
        private String email;
        private Gender gender;
        private boolean isSMSReceive;
        private String address;
        private String addressDetail;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        private LocalDateTime registeredDate;
        private Set<MemberBranchInfo> memberBranchList;
    }

    @Data
    public static class MemberBranchInfo {

        private Long branchId;
        private String branchName;

        public MemberBranchInfo(final Branch branch) {
            this.branchId = branch.getId();
            this.branchName = branch.getBranchName();
        }
    }

    @Data
    @AllArgsConstructor
    public static class MemberTicketInfo {

        private Long ticketId;
        private String ticketName;
        private Integer usageCount;
        private Integer usagePeriod;
        private String representativeTrainer;
        private Integer remainingCount;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        private LocalDate expireDate;
        private Integer attendanceCount;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MemberDetailAdditionInfo {

        private String memo;
        private List<MemberTrainerInfo> trainers;
        private Integer ticketTotalRemainingCount;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        private LocalDate ticketExpireDate;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        private LocalDate lockerExpireDate;
        private Integer totalAttendanceCount;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        private LocalDateTime firstPaymentDate;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        private LocalDate lastAttendanceDate;
        private List<MemberReservationInfo> reservations;
        private List<MemberActiveTicketInfo> activeTickets;
    }

    @Data
    public static class MemberTrainerInfo {

        private Long trainerId;
        private String trainerName;

        public MemberTrainerInfo(final Employee employee) {
            this.trainerId = employee.getId();
            this.trainerName = employee.getUser().getName();
        }
    }

    @Data
    @AllArgsConstructor
    public static class MemberReservationInfo {

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime classDateTime;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime reservedDateTime;
        private String className;
        private String representativeTrainer;
    }

    @Data
    @AllArgsConstructor
    public static class MemberActiveTicketInfo {

        private CourseClassType courseClassType;
        private String ticketName;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        private LocalDate ticketExpireDate;
        private Integer remainingCount;
    }
}
