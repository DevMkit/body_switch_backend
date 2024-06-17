package kr.co.softhubglobal.dto.course;

import kr.co.softhubglobal.entity.course.CourseClass;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class CourseClassInfoMapper implements Function<CourseClass, CourseClassDTO.CourseClassInfo> {

    @Override
    public CourseClassDTO.CourseClassInfo apply(CourseClass courseClass) {
        return new CourseClassDTO.CourseClassInfo(
                courseClass.getId(),
                courseClass.getClassName(),
                courseClass.getExerciseRoom().getName(),
                courseClass.getEmployee().getUser().getName(),
                0,
                courseClass.getMembersMaxCount(),
                courseClass.getCourseClassTimeList().get(0).getStartTime(),
                courseClass.getCourseClassTimeList().get(0).getEndTime()
        );
    }
}
