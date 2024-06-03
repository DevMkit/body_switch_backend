package kr.co.softhubglobal.repository;

import kr.co.softhubglobal.entity.course.CourseTicket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CourseTicketRepository extends JpaRepository<CourseTicket, Long>, JpaSpecificationExecutor<CourseTicket> {
}
