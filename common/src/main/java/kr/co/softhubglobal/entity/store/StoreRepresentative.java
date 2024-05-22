package kr.co.softhubglobal.entity.store;

import jakarta.persistence.*;
import kr.co.softhubglobal.entity.common.BaseDateEntity;
import kr.co.softhubglobal.entity.user.User;
import lombok.*;

@Entity
@Table(name = "store_representative")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class StoreRepresentative extends BaseDateEntity {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "USERNAME")
    private User user;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "STORE_ID")
    private Store store;
}
