package kr.co.softhubglobal.repository;

import kr.co.softhubglobal.entity.member.MemberOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberOrderRepository extends JpaRepository<MemberOrder, Long> {

    Optional<MemberOrder> findByOrderId(String orderId);
}
