package kr.co.softhubglobal.dto.course;

import kr.co.softhubglobal.entity.course.CourseClassTime;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class CourseClassTimeInfoMapper implements Function<CourseClassTime, CourseClassDTO.CourseClassTimeInfo> {

    @Override
    public CourseClassDTO.CourseClassTimeInfo apply(CourseClassTime courseClassTime) {
        return new CourseClassDTO.CourseClassTimeInfo(
                courseClassTime.getId(),
                courseClassTime.getCourseClass().getClassName(),
                courseClassTime.getCourseClass().getExerciseRoom().getName(),
                courseClassTime.getCourseClass().getEmployee().getUser().getName(),
                courseClassTime.getCourseClassTimeMembers().size(),
                courseClassTime.getCourseClass().getMembersMaxCount(),
                courseClassTime.getStartTime(),
                courseClassTime.getEndTime()
        );
    }
}
