package kr.co.softhubglobal.repository.member;

import kr.co.softhubglobal.entity.course.CourseTicket;
import kr.co.softhubglobal.entity.member.Member;
import kr.co.softhubglobal.entity.member.MemberSavedCourseTicket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberSavedCourseTicketRepository extends JpaRepository<MemberSavedCourseTicket, Long> {

    boolean existsByMemberAndCourseTicket(Member member, CourseTicket courseTicket);
    void deleteByMemberAndCourseTicket(Member member, CourseTicket courseTicket);
}
