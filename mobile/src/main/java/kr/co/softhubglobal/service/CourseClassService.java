package kr.co.softhubglobal.service;

import kr.co.softhubglobal.dto.course.CourseClassDTO;
import kr.co.softhubglobal.dto.course.CourseClassInfoMapper;
import kr.co.softhubglobal.dto.course.CourseClassTimeInfoMapper;
import kr.co.softhubglobal.entity.common.Restrictions;
import kr.co.softhubglobal.entity.course.CourseClass;
import kr.co.softhubglobal.entity.member.Member;
import kr.co.softhubglobal.entity.member.MemberCourseTicket;
import kr.co.softhubglobal.exception.customExceptions.DuplicateResourceException;
import kr.co.softhubglobal.repository.CourseClassRepository;
import kr.co.softhubglobal.repository.CourseClassTimeRepository;
import kr.co.softhubglobal.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class CourseClassService {

    private final MemberRepository memberRepository;
    private final CourseClassTimeRepository courseClassTimeRepository;
    private final CourseClassTimeInfoMapper courseClassTimeInfoMapper;
    private final MessageSource messageSource;

    public List<CourseClassDTO.CourseClassTimeInfo> getCourseClasses(
            Long userId,
            CourseClassDTO.CourseClassSearchRequest courseClassSearchRequest
    ) {

        Member member = memberRepository.findByUserId(userId)
                .orElseThrow(() -> new DuplicateResourceException(
                        messageSource.getMessage("member.user.id.not.found", new Object[]{userId}, Locale.ENGLISH)));

        Restrictions restrictions = new Restrictions();
        if(courseClassSearchRequest.getBranchId() != null) {
            restrictions.eq("courseClass.branch.id", courseClassSearchRequest.getBranchId());
        }
        if(courseClassSearchRequest.getType() != null) {
            restrictions.eq("courseClass.classType", courseClassSearchRequest.getType());
        }
        if(courseClassSearchRequest.getClassDate() != null) {
            restrictions.le("courseClass.registrationStartDate", courseClassSearchRequest.getClassDate());
            restrictions.ge("courseClass.registrationEndDate", courseClassSearchRequest.getClassDate());
            restrictions.eq("dayOfWeek", courseClassSearchRequest.getClassDate().getDayOfWeek());
        }
        restrictions.in("courseClass.courseTicket.id", member.getCourseTickets().stream().map(memberCourseTicket -> memberCourseTicket.getCourseTicket().getId()).toList());

        return courseClassTimeRepository.findAll(restrictions.output())
                .stream()
                .map(courseClassTimeInfoMapper)
                .toList();
    }
}
