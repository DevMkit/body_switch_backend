package kr.co.softhubglobal.repository;

import kr.co.softhubglobal.entity.branch.Branch;
import kr.co.softhubglobal.entity.branch.BranchType;
import kr.co.softhubglobal.entity.center.Center;
import kr.co.softhubglobal.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BranchRepository extends JpaRepository<Branch, Long> {

    boolean existsByCenter(Center center);
}
