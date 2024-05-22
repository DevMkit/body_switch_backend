package kr.co.softhubglobal.repository;

import kr.co.softhubglobal.entity.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, String> {
}
