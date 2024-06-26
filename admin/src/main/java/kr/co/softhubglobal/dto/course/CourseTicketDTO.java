package kr.co.softhubglobal.dto.course;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import kr.co.softhubglobal.dto.PageableDTO;
import kr.co.softhubglobal.dto.member.MemberDTO;
import kr.co.softhubglobal.entity.course.CourseClassType;
import kr.co.softhubglobal.entity.course.SaleStatus;
import kr.co.softhubglobal.entity.member.Gender;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

public class CourseTicketDTO {

    @Data
    public static class CourseTicketCreateRequest {

        @NotNull(message = "course.ticket.branch.id.not.null")
        private Long branchId;

        @NotNull(message = "course.ticket.class.type.not.null")
        private CourseClassType courseClassType;

        @NotNull(message = "course.ticket.name.not.null")
        @NotEmpty(message = "course.ticket.name.not.empty")
        private String ticketName;

        @NotNull(message = "course.ticket.regular.price.not.null")
        private Double regularPrice;
        @NotNull(message = "course.ticket.discount.rate.not.null")
        private Double discountRate;
        @NotNull(message = "course.ticket.final.price.not.null")
        private Double finalPrice;

        @NotNull(message = "course.ticket.sale.start.date.not.null")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        private LocalDate saleStartDate;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        private LocalDate saleEndDate;

        private boolean hasSaleEndDate;

        @NotNull(message = "course.ticket.usage.period.not.null")
        private Integer usagePeriod;

        @NotNull(message = "course.ticket.usage.count.not.null")
        private Integer usageCount;

        private List<Long> trainers;
        private List<MultipartFile> images;
        private List<CourseTicketCurriculumCreateInfo> curriculums;
        private String classDetail;
    }

    @Data
    public static class CourseTicketCurriculumCreateInfo {

        @NotNull(message = "course.ticket.curriculum.title.not.null")
        @NotEmpty(message = "course.ticket.curriculum.title.not.empty")
        private String title;

        private String summary;

        private MultipartFile image;
    }

    @Data
    @EqualsAndHashCode(callSuper = false)
    public static class CourseTicketSearchRequest extends PageableDTO.Request {

        private Long headBranchId;
        private Long branchId;
        private String ticketName;
        private List<CourseClassType> classTypes;
        private List<SaleStatus> saleStatuses;
    }

    @Data
    @AllArgsConstructor
    public static class CourseTicketInfo {

        private Long id;
        private String branchName;
        private CourseClassType classType;
        private String ticketName;
        private int usagePeriod;
        private int usageCount;
        private double finalPrice;
        private int wishListAddedCount;
        private SaleStatus saleStatus;
    }
}
