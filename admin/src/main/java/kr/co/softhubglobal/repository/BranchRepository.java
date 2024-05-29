package kr.co.softhubglobal.repository;

import kr.co.softhubglobal.entity.branch.Branch;
import kr.co.softhubglobal.entity.branch.BranchType;
import kr.co.softhubglobal.entity.center.Center;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BranchRepository extends JpaRepository<Branch, Long> {

    boolean existsByCenterAndBranchType(Center center, BranchType branchType);
}
