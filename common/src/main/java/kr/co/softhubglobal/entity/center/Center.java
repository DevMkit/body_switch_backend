package kr.co.softhubglobal.entity.center;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import kr.co.softhubglobal.entity.branch.Branch;
import kr.co.softhubglobal.entity.common.BaseDateEntity;
import lombok.*;

@Entity
@Table(name = "center")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Center extends BaseDateEntity {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "BUSINESS_NAME")
    private String businessName;

    @Column(name = "BUSINESS_NUMBER")
    private String businessNumber;

    @Column(name = "BUSINESS_CLASSIFICATION")
    @Enumerated(EnumType.STRING)
    private BusinessClassification businessClassification;

    @Column(name = "BUSINESS_TYPE")
    private String businessType;

    @Column(name = "POSTAL_CODE")
    private String postalCode;

    @Column(name = "CITY")
    private String city;

    @Column(name = "ADDRESS")
    private String address;

    @Column(name = "ADDRESS_DETAIL")
    private String addressDetail;

    @Column(name = "REPRESENTATIVE_NUMBER")
    private String representativeNumber;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "HOMEPAGE")
    private String homepage;

    @Column(name = "CENTER_TYPE")
    @Enumerated(EnumType.STRING)
    private CenterType centerType;

    @Column(name = "HEAD_CENTER_ID")
    private Long headCenterId;

    @Column(name = "REG_ID")
    private String registeredId;

    @Column(name = "UPD_ID")
    private String updatedId;

    @OneToOne(mappedBy = "center", cascade = CascadeType.ALL)
    private Branch branch;

    @OneToOne(mappedBy = "center", cascade = CascadeType.ALL)
    private CenterManager centerManager;
}
