package kr.co.softhubglobal.service;

import kr.co.softhubglobal.dto.course.*;
import kr.co.softhubglobal.entity.common.Restrictions;
import kr.co.softhubglobal.entity.course.CourseClassTime;
import kr.co.softhubglobal.entity.course.CourseClassTimeMember;
import kr.co.softhubglobal.entity.member.Member;
import kr.co.softhubglobal.entity.member.MemberCourseTicket;
import kr.co.softhubglobal.entity.member.MemberReservation;
import kr.co.softhubglobal.entity.member.ReservationStatus;
import kr.co.softhubglobal.exception.customExceptions.RequestNotAcceptableException;
import kr.co.softhubglobal.exception.customExceptions.ResourceNotFoundException;
import kr.co.softhubglobal.repository.*;
import kr.co.softhubglobal.repository.member.MemberCourseTicketRepository;
import kr.co.softhubglobal.repository.member.MemberRepository;
import kr.co.softhubglobal.repository.member.MemberReservationRepository;
import kr.co.softhubglobal.validator.ObjectValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class CourseClassService {

    private final MemberRepository memberRepository;
    private final MemberReservationRepository memberReservationRepository;
    private final MemberCourseTicketRepository memberCourseTicketRepository;
    private final CourseClassTimeRepository courseClassTimeRepository;
    private final CourseClassTimeMemberRepository courseClassTimeMemberRepository;
    private final CourseClassTimeInfoMapper courseClassTimeInfoMapper;
    private final CourseClassTimeDetailInfoMapper courseClassTimeDetailInfoMapper;
    private final ObjectValidator<CourseClassDTO.CourseClassTimeReserveRequest> courseClassTimeReserveRequestObjectValidator;
    private final MessageSource messageSource;

    public List<CourseClassDTO.CourseClassTimeInfo> getCourseClasses(
            Long userId,
            CourseClassDTO.CourseClassSearchRequest courseClassSearchRequest
    ) {
        Member member = memberRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
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
        restrictions.notIn("id", member.getReservations()
                .stream()
                .filter(memberReservation -> memberReservation.getStatus().equals(ReservationStatus.RESERVED))
                .map(memberReservation -> memberReservation.getCourseClassTime().getId())
                .toList()
        );

        return courseClassTimeRepository.findAll(restrictions.output())
                .stream()
                .map(courseClassTimeInfoMapper)
                .toList();
    }

    public CourseClassDTO.CourseClassTimeDetailInfo getCourseTicketDetailInfoById(Long userId, Long courseClassTimeId) {
        Member member = memberRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        messageSource.getMessage("member.user.id.not.found", new Object[]{userId}, Locale.ENGLISH)));

        return courseClassTimeRepository.findById(courseClassTimeId)
                .map(courseClassTime -> courseClassTimeDetailInfoMapper.apply(
                        courseClassTime,
                        member.getCourseTickets()
                                .stream()
                                .filter(memberCourseTicket -> memberCourseTicket.getCourseTicket().equals(courseClassTime.getCourseClass().getCourseTicket()))
                                .toList()
                                .get(0)
                ))
                .orElseThrow(() -> new ResourceNotFoundException(
                        messageSource.getMessage("course.class.time.id.not.exist", new Object[]{courseClassTimeId}, Locale.ENGLISH))
                );
    }

    @Transactional
    public void reserveCourseClassTime(Long userId, CourseClassDTO.CourseClassTimeReserveRequest courseClassTimeReserveRequest) {

        courseClassTimeReserveRequestObjectValidator.validate(courseClassTimeReserveRequest);

        Member member = memberRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        messageSource.getMessage("member.user.id.not.found", new Object[]{userId}, Locale.ENGLISH)));

        CourseClassTime courseClassTime = courseClassTimeRepository.findById(courseClassTimeReserveRequest.getCourseClassTimeId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        messageSource.getMessage("course.class.time.id.not.exist", new Object[]{courseClassTimeReserveRequest.getCourseClassTimeId()}, Locale.ENGLISH))
                );

        if(courseClassTime.getCourseClassTimeMembers().stream().anyMatch(courseClassTimeMember -> courseClassTimeMember.getMember().equals(member))) {
            throw new RequestNotAcceptableException(
                    messageSource.getMessage("course.class.time.member.already.reserved", null, Locale.ENGLISH));
        };

        if(courseClassTime.getCourseClassTimeMembers().size() >= courseClassTime.getCourseClass().getMembersMaxCount()) {
            throw new RequestNotAcceptableException(
                    messageSource.getMessage("course.class.time.member.full", null, Locale.ENGLISH));
        }

        MemberCourseTicket memberCourseTicket = member.getCourseTickets()
                .stream()
                .filter(currentMemberCourseTicket -> currentMemberCourseTicket.getCourseTicket().equals(courseClassTime.getCourseClass().getCourseTicket()))
                .toList()
                .get(0);

        memberCourseTicket.setUsedCount(memberCourseTicket.getUsedCount() + 1);

        memberReservationRepository.save(MemberReservation.builder()
               .member(member)
               .courseClassTime(courseClassTime)
               .reservationDate(courseClassTimeReserveRequest.getReservationDate())
               .status(ReservationStatus.RESERVED)
               .build()
        );

        courseClassTimeMemberRepository.save(CourseClassTimeMember.builder()
                .member(member)
                .courseClassTime(courseClassTime)
                .build()
        );

        memberCourseTicketRepository.save(memberCourseTicket);
    }
}
