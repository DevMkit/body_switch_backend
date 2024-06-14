package kr.co.softhubglobal.entity.branch;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import kr.co.softhubglobal.entity.center.Center;
import kr.co.softhubglobal.entity.center.CenterManager;
import kr.co.softhubglobal.entity.common.BaseEntity;
import kr.co.softhubglobal.entity.course.CourseTicket;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "branch")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Branch extends BaseEntity {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "CENTER_ID")
    @JsonBackReference
    private Center center;

    @Column(name = "BRANCH_NAME")
    private String branchName;

    @Column(name = "BRANCH_DESCRIPTION")
    private String branchDescription;

    @Column(name = "POSTAL_CODE")
    private String postalCode;

    @Column(name = "CITY")
    private String city;

    @Column(name = "ADDRESS")
    private String address;

    @Column(name = "ADDRESS_DETAIL")
    private String addressDetail;

    @Column(name = "RESERVATION_POLICY")
    private String reservationPolicy;

    @Column(name = "BRANCH_DETAIL_DESCRIPTION")
    private String branchDetailDescription;

    @OneToMany(mappedBy = "branch", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<BranchFacility> branchFacilities;

    @OneToMany(mappedBy = "branch", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<BranchWorkHours> branchWorkHoursList;

    @OneToMany(mappedBy = "branch", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<BranchExerciseRoom> branchExerciseRooms;

    @OneToMany(mappedBy = "branch", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<CourseTicket> courseTickets;
}
