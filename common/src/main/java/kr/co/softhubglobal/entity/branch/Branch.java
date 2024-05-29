package kr.co.softhubglobal.entity.branch;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import kr.co.softhubglobal.entity.center.Center;
import kr.co.softhubglobal.entity.common.BaseEntity;
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

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "CENTER_ID")
    public Center center;

    @Column(name = "BRANCH_NAME")
    public String branchName;

    @Column(name = "BRANCH_DESCRIPTION")
    public String branchDescription;

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

    @Column(name = "TYPE")
    @Enumerated(EnumType.STRING)
    private BranchType branchType;

    @OneToMany(mappedBy = "branch", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<BranchFacility> branchFacilities;

    @OneToMany(mappedBy = "branch", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<BranchWorkHours> branchWorkHoursList;
}