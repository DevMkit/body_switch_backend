package kr.co.softhubglobal.repository.member;

import kr.co.softhubglobal.entity.member.MemberReservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface MemberReservationRepository extends JpaRepository<MemberReservation, Long>, JpaSpecificationExecutor<MemberReservation> {

    List<MemberReservation> findByMemberUserId(Long userId);

}
