package kr.co.softhubglobal.repository.member;

import kr.co.softhubglobal.entity.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, String> {

    Optional<Member> findByUserId(Long userId);
}
