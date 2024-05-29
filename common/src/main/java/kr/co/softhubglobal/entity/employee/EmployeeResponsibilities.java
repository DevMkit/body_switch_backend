package kr.co.softhubglobal.entity.employee;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import kr.co.softhubglobal.entity.common.BaseDateEntity;
import lombok.*;

@Entity
@Table(name = "employee_responsibilities")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class EmployeeResponsibilities extends BaseDateEntity {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "EMPLOYEE_ID")
    @JsonBackReference
    public Employee employee;

    @Column(name = "NAME")
    private String name;
}
