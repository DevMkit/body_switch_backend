package kr.co.softhubglobal.entity.course;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import kr.co.softhubglobal.entity.branch.Branch;
import kr.co.softhubglobal.entity.common.BaseEntity;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "course_ticket")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CourseTicket extends BaseEntity {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "BRANCH_ID")
    @JsonBackReference
    public Branch branch;

    @Column(name = "CLASS_TYPE")
    @Enumerated(EnumType.STRING)
    private CourseClassType classType;

    @Column(name = "TICKET_NAME")
    public String ticketName;

    @Column(name = "REGULAR_PRICE")
    public Double regularPrice;

    @Column(name = "DISCOUNT_RATE")
    public Double discountRate;

    @Column(name = "FINAL_PRICE")
    public Double finalPrice;

    @Column(name = "SALE_START_DATE")
    private LocalDate saleStartDate;

    @Column(name = "SALE_END_DATE")
    private LocalDate saleEndDate;

    @Column(name = "HAS_SALE_END_DATE")
    private boolean hasSaleEndDate;

    @Column(name = "SALE_STATUS")
    @Enumerated(EnumType.STRING)
    private SaleStatus saleStatus;

    @Column(name = "USAGE_PERIOD")
    public Integer usagePeriod;

    @Column(name = "USAGE_COUNT")
    public Integer usageCount;

    @Column(name = "CLASS_DETAIL")
    public String classDetail;

    @Column(name = "IS_POST")
    private boolean isPost;

    @OneToMany(mappedBy = "courseTicket", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<CourseTrainer> courseTrainers;

    @OneToMany(mappedBy = "courseTicket", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<CourseTicketImage> courseTicketImages;

    @OneToMany(mappedBy = "courseTicket", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<CourseCurriculum> courseCurriculumList;
}
