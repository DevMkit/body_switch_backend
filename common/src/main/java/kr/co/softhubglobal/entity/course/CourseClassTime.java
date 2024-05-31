package kr.co.softhubglobal.entity.course;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import kr.co.softhubglobal.entity.common.BaseDateEntity;
import lombok.*;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Entity
@Table(name = "course_class_time")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CourseClassTime extends BaseDateEntity {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "COURSE_CLASS_ID")
    @JsonBackReference
    public CourseClass courseClass;

    @Column(name = "DAY_OF_WEEK")
    @Enumerated(EnumType.STRING)
    private DayOfWeek dayOfWeek;

    @Column(name = "START_TIME")
    private LocalTime closeTime;

    @Column(name = "END_TIME")
    private LocalTime openTime;
}
