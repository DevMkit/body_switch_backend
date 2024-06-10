package kr.co.softhubglobal.repository;

import kr.co.softhubglobal.entity.center.Center;
import kr.co.softhubglobal.entity.center.CenterManager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface CenterManagerRepository extends JpaRepository<CenterManager, Long>, JpaSpecificationExecutor<CenterManager> {

    Optional<CenterManager> findByUserUsername(String username);

    Optional<CenterManager> findByCenter(Center center);
}
