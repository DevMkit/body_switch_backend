package kr.co.softhubglobal.entity.member;

import jakarta.persistence.*;
import kr.co.softhubglobal.entity.common.BaseDateEntity;
import kr.co.softhubglobal.entity.user.User;
import lombok.*;
import org.hibernate.annotations.Formula;

import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "member")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Member extends BaseDateEntity {

    @Id
    @Column(name = "ID")
    private String id;

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

    @Column(name = "POSTAL_CODE")
    private String postalCode;

    @Column(name = "ADDRESS")
    private String address;

    @Column(name = "ADDRESS_DETAIL")
    private String addressDetail;

    @Column(name = "STATUS")
    @Enumerated(EnumType.STRING)
    private MemberStatus status;

    @Column(name = "LOGIN_PROVIDER")
    @Enumerated(EnumType.STRING)
    private LoginProvider loginProvider;

    @Column(name = "IS_SMS_RECEIVE")
    private boolean isSMSReceive;

    @Column(name = "REG_TYPE")
    @Enumerated(EnumType.STRING)
    private MemberRegisteredType regType;

    @Column(name = "REG_ID")
    private String registeredId;

    @Column(name = "UPD_ID")
    private String updatedId;

    @Formula("(SELECT FLOOR(DATEDIFF(CURRENT_DATE(), m.BIRTH_DATE) / 362.25) FROM member m WHERE m.ID = ID)")
    private Integer age;
}
