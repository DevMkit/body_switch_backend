package kr.co.softhubglobal.entity.center;

import jakarta.persistence.*;
import kr.co.softhubglobal.entity.branch.Branch;
import kr.co.softhubglobal.entity.common.BaseDateEntity;
import kr.co.softhubglobal.entity.user.User;
import lombok.*;

@Entity
@Table(name = "center_manager")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CenterManager extends BaseDateEntity {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "USER_ID")
    private User user;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "CENTER_ID")
    private Center center;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "BRANCH_ID")
    private Branch branch;

    @Column(name = "MANAGER_TYPE")
    @Enumerated(EnumType.STRING)
    private ManagerType managerType;
}
