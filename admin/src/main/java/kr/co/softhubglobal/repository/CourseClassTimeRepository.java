package kr.co.softhubglobal.repository;

import kr.co.softhubglobal.entity.branch.BranchExerciseRoom;
import kr.co.softhubglobal.entity.course.CourseClassTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface CourseClassTimeRepository extends JpaRepository<CourseClassTime, Long> {

    boolean existsByCourseClassExerciseRoomAndCourseClassRegistrationStartDateBetweenAndCourseClassRegistrationEndDateBetweenAndDayOfWeekAndStartTimeBetweenAndEndTimeBetween(
            BranchExerciseRoom branchExerciseRoom,
            LocalDate registrationStartDate,
            LocalDate registrationEndDate,
            LocalDate registrationStartDate1,
            LocalDate registrationEndDate1,
            DayOfWeek dayOfWeek,
            LocalTime startTime,
            LocalTime endTime,
            LocalTime startTime1,
            LocalTime endTime1
    );

    @Query(value = "SELECT cc.ID "
            + "FROM course_class cc "
            + "INNER JOIN course_class_time cct ON cc.ID = cct.COURSE_CLASS_ID "
            + "WHERE cc.BRANCH_EXERCISE_ROOM_ID = ?1 "
            + "AND (cc.REGISTRATION_START_DATE BETWEEN ?2 AND ?3 OR cc.REGISTRATION_END_DATE BETWEEN ?2 AND ?3) "
            + "AND cct.DAY_OF_WEEK = ?4 "
            + "AND (cct.START_TIME BETWEEN ?5 AND ?6 OR cct.END_TIME BETWEEN ?5 AND ?6)"
            , nativeQuery = true)
    List<Long> existsByScheduledClassTime(
            Long exerciseRoomId,
            LocalDate registrationStartDate,
            LocalDate registrationEndDate,
            String dayOfWeek,
            LocalTime startTime,
            LocalTime endTime
    );
}
