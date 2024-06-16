package kr.co.softhubglobal.service;

import kr.co.softhubglobal.dto.course.CourseTicketDTO;
import kr.co.softhubglobal.dto.course.CourseTicketDetailInfoMapper;
import kr.co.softhubglobal.dto.course.CourseTicketInfoMapper;
import kr.co.softhubglobal.entity.common.Restrictions;
import kr.co.softhubglobal.entity.course.SaleStatus;
import kr.co.softhubglobal.entity.member.Member;
import kr.co.softhubglobal.exception.customExceptions.DuplicateResourceException;
import kr.co.softhubglobal.exception.customExceptions.ResourceNotFoundException;
import kr.co.softhubglobal.repository.CourseTicketRepository;
import kr.co.softhubglobal.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class CourseTicketService {

    private final CourseTicketRepository courseTicketRepository;
    private final MemberRepository memberRepository;
    private final CourseTicketInfoMapper courseTicketInfoMapper;
    private final CourseTicketDetailInfoMapper courseTicketDetailInfoMapper;
    private final MessageSource messageSource;

    public List<CourseTicketDTO.CourseTicketInfo> getCourseTickets(
            Long userId,
            CourseTicketDTO.CourseTicketSearchRequest courseTicketSearchRequest
    ) {
        Member member = memberRepository.findByUserId(userId)
                .orElseThrow(() -> new DuplicateResourceException(
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

    public CourseTicketDTO.CourseTicketDetailInfo getCourseTicketDetailInfoById(Long courseTicketId) {
        return courseTicketRepository.findById(courseTicketId)
                .map(courseTicketDetailInfoMapper)
                .orElseThrow(() -> new ResourceNotFoundException(
                        messageSource.getMessage("course.ticket.id.not.exist", new Object[]{courseTicketId}, Locale.ENGLISH))
                );
    }
}

