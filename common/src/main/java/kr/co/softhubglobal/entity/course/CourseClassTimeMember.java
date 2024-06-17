package kr.co.softhubglobal.entity.course;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import kr.co.softhubglobal.entity.common.BaseDateEntity;
import kr.co.softhubglobal.entity.member.Member;
import lombok.*;

@Entity
@Table(name = "course_class_time_member")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CourseClassTimeMember extends BaseDateEntity {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "COURSE_CLASS_TIME_ID")
    @JsonBackReference
    public CourseClassTime courseClassTime;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;
}
