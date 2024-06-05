package kr.co.softhubglobal.entity.member;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import kr.co.softhubglobal.entity.common.BaseDateEntity;
import kr.co.softhubglobal.entity.course.CourseTicket;
import lombok.*;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "member_order")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberOrder extends BaseDateEntity {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "MEMBER_ID")
    @JsonBackReference
    private Member member;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "COURSE_TICKET_ID")
    private CourseTicket courseTicket;

    @Column(name = "ORDER_ID")
    private String orderId;

    @Column(name = "TID")
    private String tid;

    @Column(name = "PAYMENT_METHOD")
    private String paymentMethod;

    @Column(name = "PAYMENT_STATUS")
    private String paymentStatus;

    @Column(name = "RESULT_CODE")
    private String resultCode;

    @Column(name = "RESULT_MSG")
    private String resultMsg;

    @Column(name = "PAID_AT")
    private LocalDateTime paidAT;
}
