package kr.co.softhubglobal.entity.course;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import kr.co.softhubglobal.entity.common.BaseDateEntity;
import kr.co.softhubglobal.entity.employee.Employee;
import lombok.*;

@Entity
@Table(name = "course_trainer")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CourseTrainer extends BaseDateEntity {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "COURSE_TICKET_ID")
    @JsonBackReference
    public CourseTicket courseTicket;

    @ManyToOne
    @JoinColumn(name = "EMPLOYEE_ID")
    private Employee employee;
}
