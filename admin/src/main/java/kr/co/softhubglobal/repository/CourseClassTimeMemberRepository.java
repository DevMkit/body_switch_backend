package kr.co.softhubglobal.repository;

import kr.co.softhubglobal.entity.course.CourseClassTimeMember;
import kr.co.softhubglobal.entity.employee.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CourseClassTimeMemberRepository extends JpaRepository<CourseClassTimeMember, Long> {

    Page<CourseClassTimeMember> findByCourseClassTimeCourseClassEmployee(Employee employee, Pageable pageable);
}
