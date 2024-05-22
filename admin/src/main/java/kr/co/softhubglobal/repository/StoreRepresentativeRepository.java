package kr.co.softhubglobal.repository;

import kr.co.softhubglobal.entity.store.StoreRepresentative;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StoreRepresentativeRepository extends JpaRepository<StoreRepresentative, Long> {

    Optional<StoreRepresentative> findByUserUsername(String username);
}
