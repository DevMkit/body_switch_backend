package kr.co.softhubglobal.repository;

import kr.co.softhubglobal.entity.course.CourseClassTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CourseClassTimeRepository extends JpaRepository<CourseClassTime, Long>, JpaSpecificationExecutor<CourseClassTime> {
}
