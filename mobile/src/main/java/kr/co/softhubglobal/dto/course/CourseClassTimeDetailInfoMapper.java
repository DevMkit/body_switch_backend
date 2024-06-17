package kr.co.softhubglobal.dto.course;

import kr.co.softhubglobal.entity.course.CourseClassTime;
import kr.co.softhubglobal.entity.member.MemberCourseTicket;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class CourseClassTimeDetailInfoMapper implements Function<CourseClassTime, CourseClassDTO.CourseClassTimeDetailInfo> {

    private final ActiveCourseTicketInfoMapper activeCourseTicketInfoMapper;

    public CourseClassDTO.CourseClassTimeDetailInfo apply(CourseClassTime courseClassTime, MemberCourseTicket memberCourseTicket) {
        return new CourseClassDTO.CourseClassTimeDetailInfo(
                courseClassTime.getId(),
                courseClassTime.getCourseClass().getClassType(),
                courseClassTime.getCourseClass().getBranch().getBranchName(),
                courseClassTime.getCourseClass().getClassName(),
                courseClassTime.getStartTime(),
                courseClassTime.getEndTime(),
                courseClassTime.getCourseClass().getExerciseRoom().getName(),
                courseClassTime.getCourseClass().getEmployee().getUser().getName(),
                activeCourseTicketInfoMapper.apply(memberCourseTicket)
        );
    }

    @Override
    public CourseClassDTO.CourseClassTimeDetailInfo apply(CourseClassTime courseClassTime) {
        return null;
    }
}
