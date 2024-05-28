package kr.co.softhubglobal.entity.branch;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import kr.co.softhubglobal.entity.common.BaseEntity;
import lombok.*;

import java.sql.Time;
import java.time.DayOfWeek;
import java.time.LocalTime;

@Entity
@Table(name = "branch_work_hours")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class BranchWorkHours extends BaseEntity {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "BRANCH_ID")
    @JsonBackReference
    public Branch branch;

    @Column(name = "DAY_OF_WEEK")
    @Enumerated(EnumType.STRING)
    private DayOfWeek dayOfWeek;

    @Column(name = "IS_OPEN")
    private Boolean isOpen;

    @Column(name = "OPEN_TIME")
    private LocalTime openTime;

    @Column(name = "CLOSE_TIME")
    private LocalTime closeTime;
}
