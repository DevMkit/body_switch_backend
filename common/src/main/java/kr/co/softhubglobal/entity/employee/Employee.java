package kr.co.softhubglobal.entity.employee;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import kr.co.softhubglobal.entity.branch.Branch;
import kr.co.softhubglobal.entity.common.BaseEntity;
import kr.co.softhubglobal.entity.member.Gender;
import kr.co.softhubglobal.entity.user.User;
import lombok.*;
import org.hibernate.annotations.Formula;

import java.time.LocalDate;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "employee")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Employee extends BaseEntity {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "USER_ID")
    private User user;

    @Column(name = "PROFILE_IMAGE")
    private String profileImage;

    @Column(name = "BIRTH_DATE")
    private LocalDate birthDate;

    @Column(name = "GENDER")
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @ManyToOne
    @JoinColumn(name = "BRANCH_ID")
    public Branch branch;

    @Column(name = "INTRODUCTION")
    private String introduction;

    @Column(name = "EMPLOYEE_CLASSIFICATION")
    @Enumerated(EnumType.STRING)
    private EmployeeClassification employeeClassification;

    @Column(name = "HIRED_DATE")
    private LocalDate hiredDate;

    @Column(name = "LEFT_DATE")
    private LocalDate leftDate;

    @Formula("(SELECT FLOOR(DATEDIFF(CURRENT_DATE(), m.BIRTH_DATE) / 362.25) FROM member m WHERE m.ID = ID)")
    private Integer age;

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<EmployeeResponsibility> responsibilities;
}
