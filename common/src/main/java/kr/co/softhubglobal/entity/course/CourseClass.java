package kr.co.softhubglobal.entity.course;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import kr.co.softhubglobal.entity.branch.Branch;
import kr.co.softhubglobal.entity.branch.BranchExerciseRoom;
import kr.co.softhubglobal.entity.common.BaseEntity;
import kr.co.softhubglobal.entity.employee.Employee;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "course_class")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CourseClass extends BaseEntity {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "BRANCH_ID")
    public Branch branch;

    @Column(name = "CLASS_TYPE")
    @Enumerated(EnumType.STRING)
    private CourseClassType classType;

    @Column(name = "CLASS_NAME")
    private String className;

    @ManyToOne
    @JoinColumn(name = "BRANCH_EXERCISE_ROOM_ID")
    public BranchExerciseRoom exerciseRoom;

    @Column(name = "REGISTRATION_START_DATE")
    private LocalDate registrationStartDate;

    @Column(name = "REGISTRATION_END_DATE")
    private LocalDate registrationEndDate;

    @ManyToOne
    @JoinColumn(name = "REPRESENTATIVE_TRAINER_ID")
    private Employee employee;

    @ManyToOne
    @JoinColumn(name = "COURSE_TICKET_ID")
    private CourseTicket courseTicket;

    @Column(name = "PRIVATE_MEMBER_ID")
    private String privateMemberId;

    @Column(name = "MEMBERS_MAX_COUNT")
    private Integer membersMaxCount;

    @Column(name = "MEMO")
    private String memo;

    @OneToMany(mappedBy = "courseClass", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<CourseClassTime> courseClassTimeList;
}
