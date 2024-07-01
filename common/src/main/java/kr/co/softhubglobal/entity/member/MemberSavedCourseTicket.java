package kr.co.softhubglobal.entity.member;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import kr.co.softhubglobal.entity.common.BaseDateEntity;
import kr.co.softhubglobal.entity.course.CourseTicket;
import lombok.*;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "member_saved_course_ticket")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberSavedCourseTicket extends BaseDateEntity {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    @JsonBackReference
    private Member member;

    @ManyToOne
    @JoinColumn(name = "COURSE_TICKET_ID")
    private CourseTicket courseTicket;
}
