package kr.co.softhubglobal.repository;

import kr.co.softhubglobal.entity.course.CourseClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CourseClassRepository extends JpaRepository<CourseClass, Long>, JpaSpecificationExecutor<CourseClass> {
}
