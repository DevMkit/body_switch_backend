package kr.co.softhubglobal.repository;

import kr.co.softhubglobal.entity.center.CenterManager;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CenterManagerRepository extends JpaRepository<CenterManager, Long> {

    Optional<CenterManager> findByUserUsername(String username);
}
