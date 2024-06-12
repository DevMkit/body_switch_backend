package kr.co.softhubglobal.repository;

import kr.co.softhubglobal.entity.center.Center;
import kr.co.softhubglobal.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface CenterRepository extends JpaRepository<Center, Long>, JpaSpecificationExecutor<Center> {

    Optional<Center> findByCenterManagerUser(User user);
}
