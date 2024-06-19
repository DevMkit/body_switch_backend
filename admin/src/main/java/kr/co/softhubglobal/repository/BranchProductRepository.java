package kr.co.softhubglobal.repository;

import kr.co.softhubglobal.entity.branch.BranchProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BranchProductRepository extends JpaRepository<BranchProduct, Long> {
}
