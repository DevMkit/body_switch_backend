package kr.co.softhubglobal.entity.employee;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import kr.co.softhubglobal.entity.common.BaseDateEntity;
import lombok.*;

@Entity
@Table(name = "employee_responsibility")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class EmployeeResponsibility extends BaseDateEntity {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "EMPLOYEE_ID")
    @JsonBackReference
    private Employee employee;

    @Column(name = "NAME")
    private String name;
}
