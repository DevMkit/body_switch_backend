package kr.co.softhubglobal.entity.member;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import kr.co.softhubglobal.entity.common.BaseDateEntity;
import kr.co.softhubglobal.entity.course.CourseTicket;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "member_course_ticket")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberCourseTicket extends BaseDateEntity {

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

    @Column(name = "START_DATE")
    private LocalDate startDate;

    @Column(name = "USED_COUNT")
    private Integer usedCount;

    @Column(name = "STATUS")
    @Enumerated(EnumType.STRING)
    private MemberCourseTicketStatus status;
}
