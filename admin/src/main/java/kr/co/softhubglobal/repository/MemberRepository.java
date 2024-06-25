package kr.co.softhubglobal.repository;

import kr.co.softhubglobal.entity.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface MemberRepository extends JpaRepository<Member, String>, JpaSpecificationExecutor<Member> {
}
