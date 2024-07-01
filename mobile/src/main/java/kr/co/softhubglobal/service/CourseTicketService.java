package kr.co.softhubglobal.service;

import kr.co.softhubglobal.dto.course.CourseTicketDTO;
import kr.co.softhubglobal.dto.course.CourseTicketDetailInfoMapper;
import kr.co.softhubglobal.dto.course.CourseTicketInfoMapper;
import kr.co.softhubglobal.entity.common.Restrictions;
import kr.co.softhubglobal.entity.course.CourseTicket;
import kr.co.softhubglobal.entity.course.SaleStatus;
import kr.co.softhubglobal.entity.member.Member;
import kr.co.softhubglobal.entity.member.MemberSavedCourseTicket;
import kr.co.softhubglobal.exception.customExceptions.ResourceNotFoundException;
import kr.co.softhubglobal.repository.CourseTicketRepository;
import kr.co.softhubglobal.repository.member.MemberRepository;
import kr.co.softhubglobal.repository.member.MemberSavedCourseTicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class CourseTicketService {

    private final CourseTicketRepository courseTicketRepository;
    private final MemberRepository memberRepository;
    private final MemberSavedCourseTicketRepository memberSavedCourseTicketRepository;
    private final CourseTicketInfoMapper courseTicketInfoMapper;
    private final CourseTicketDetailInfoMapper courseTicketDetailInfoMapper;
    private final MessageSource messageSource;

    public List<CourseTicketDTO.CourseTicketInfo> getCourseTickets(
            Long userId,
            CourseTicketDTO.CourseTicketSearchRequest courseTicketSearchRequest
    ) {
        Member member = memberRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        messageSource.getMessage("member.user.id.not.found", new Object[]{userId}, Locale.ENGLISH)));

        Restrictions restrictions = new Restrictions();

        if(courseTicketSearchRequest.getBranchId() != null) {
            restrictions.eq("branch.id", courseTicketSearchRequest.getBranchId());
        }

        restrictions.eq("saleStatus", SaleStatus.APPROVED);

        return courseTicketRepository.findAll(restrictions.output())
                .stream()
                .filter(courseTicket -> member.getCourseTickets()
                        .stream()
                        .noneMatch(memberCourseTicket -> memberCourseTicket.getCourseTicket().equals(courseTicket))
                )
                .map(courseTicketInfoMapper)
                .toList();
    }

    public CourseTicketDTO.CourseTicketDetailInfo getCourseTicketDetailInfoById(Long courseTicketId, Long userId) {
        Member member = memberRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        messageSource.getMessage("member.user.id.not.found", new Object[]{userId}, Locale.ENGLISH)));

        return courseTicketRepository.findById(courseTicketId)
                .map(courseTicket -> courseTicketDetailInfoMapper.apply(courseTicket, member))
                .orElseThrow(() -> new ResourceNotFoundException(
                        messageSource.getMessage("course.ticket.id.not.exist", new Object[]{courseTicketId}, Locale.ENGLISH))
                );
    }

    @Transactional
    public void saveCourseTicketById(Long courseTicketId, Long userId) {

        CourseTicket courseTicket = courseTicketRepository.findById(courseTicketId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        messageSource.getMessage("course.ticket.id.not.exist", new Object[]{courseTicketId}, Locale.ENGLISH)));

        Member member = memberRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        messageSource.getMessage("member.user.id.not.found", new Object[]{userId}, Locale.ENGLISH)));

        if(memberSavedCourseTicketRepository.existsByMemberAndCourseTicket(member, courseTicket)) {
            memberSavedCourseTicketRepository.deleteByMemberAndCourseTicket(member, courseTicket);
        } else {
            memberSavedCourseTicketRepository.save(
                    MemberSavedCourseTicket.builder()
                            .member(member)
                            .courseTicket(courseTicket)
                            .build()
            );
        }
    }
}

