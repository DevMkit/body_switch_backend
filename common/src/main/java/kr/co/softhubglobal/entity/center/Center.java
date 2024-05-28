package kr.co.softhubglobal.entity.center;

import jakarta.persistence.*;
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
    public String businessName;

    @Column(name = "BUSINESS_NUMBER")
    public String businessNumber;

    @Column(name = "BUSINESS_CLASSIFICATION")
    @Enumerated(EnumType.STRING)
    public BusinessClassification businessClassification;

    @Column(name = "BUSINESS_TYPE")
    public String businessType;

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

    @Column(name = "STATUS")
    @Enumerated(EnumType.STRING)
    private CenterStatus status;
}
