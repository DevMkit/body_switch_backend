package kr.co.softhubglobal.entity.branch;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import kr.co.softhubglobal.entity.common.BaseDateEntity;
import lombok.*;

@Entity
@Table(name = "branch_product")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class BranchProduct extends BaseDateEntity {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "BRANCH_ID")
    @JsonBackReference
    private Branch branch;

//    @ManyToOne
//    @JoinColumn(name = "BRANCH_CATEGORY_ID")
//    private BranchProductCategory branchProductCategory;

    @Column(name = "BRANCH_CATEGORY_ID")
    private Long branchCategoryId;

    @Column(name = "NAME")
    private String name;

    @Column(name = "PRICE")
    private Integer price;

    @Column(name = "STOCK_QUANTITY")
    private Integer stockQuantity;
}
