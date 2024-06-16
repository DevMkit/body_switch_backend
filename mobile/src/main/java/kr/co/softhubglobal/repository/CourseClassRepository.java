package kr.co.softhubglobal.repository;

import kr.co.softhubglobal.entity.course.CourseClass;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseClassRepository extends JpaRepository<CourseClass, Long> {
}
