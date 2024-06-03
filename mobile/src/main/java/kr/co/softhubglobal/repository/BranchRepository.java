package kr.co.softhubglobal.repository;

import kr.co.softhubglobal.entity.branch.Branch;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BranchRepository extends JpaRepository<Branch, Long> {
}
