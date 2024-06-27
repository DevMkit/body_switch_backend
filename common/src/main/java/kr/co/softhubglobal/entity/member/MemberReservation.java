package kr.co.softhubglobal.entity.member;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import kr.co.softhubglobal.entity.common.BaseDateEntity;
import kr.co.softhubglobal.entity.course.CourseClassTime;
import lombok.*;

import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "member_reservation")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberReservation extends BaseDateEntity {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "MEMBER_ID")
    @JsonBackReference
    private Member member;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "COURSE_CLASS_TIME_ID")
    private CourseClassTime courseClassTime;

    @Column(name = "RESERVATION_DATE")
    private LocalDate reservationDate;

    @Column(name = "STATUS")
    @Enumerated(EnumType.STRING)
    private ReservationStatus status;
}
