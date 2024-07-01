package kr.co.softhubglobal.service.member;

import kr.co.softhubglobal.dto.PageableDTO;
import kr.co.softhubglobal.dto.member.*;
import kr.co.softhubglobal.entity.course.CourseTrainer;
import kr.co.softhubglobal.entity.member.*;
import kr.co.softhubglobal.exception.customExceptions.ResourceNotFoundException;
import kr.co.softhubglobal.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final MemberInfoMapper memberInfoMapper;
    private final MemberDetailInfoMapper memberDetailInfoMapper;
    private final MemberReservationInfoMapper memberReservationInfoMapper;
    private final MemberActiveTicketInfoMapper memberActiveTicketInfoMapper;
    private final MessageSource messageSource;

    public PageableDTO.Response getAllMembers(MemberDTO.MemberSearchRequest memberSearchRequest) {

        Specification<Member> specification = Specification
                .where(MemberSpecifications.memberCourseTicketBranchCenterIdOrHeadCenterIdEqual(memberSearchRequest.getHeadBranchId()))
                .and(MemberSpecifications.memberCourseTicketBranchIdEqual(memberSearchRequest.getBranchId()))
                .and(MemberSpecifications.memberTypesEqual(memberSearchRequest.getMemberTypes()))
                .and(MemberSpecifications.memberNameOrUsernameOrPhoneNumberLike(memberSearchRequest.getSearchInput()))
                .and(MemberSpecifications.memberCourseTicketTypeIn(memberSearchRequest.getClassTypes()))
                .and(MemberSpecifications.memberCourseTicketIdEqual(memberSearchRequest.getCourseTicketId()))
                .and(MemberSpecifications.memberCourseTicketTrainerIdEqual(memberSearchRequest.getCourseTrainerId()))
                .and(MemberSpecifications.memberGenderIn(memberSearchRequest.getGenders()))
                .and(MemberSpecifications.memberAgeInRanges(memberSearchRequest.getAgeRanges()))
                .and(MemberSpecifications.memberIsSmsReceiveIn(memberSearchRequest.getSmsRecieveList()))
                .and(MemberSpecifications.memberCourseTicketRemainingCount(memberSearchRequest.getRemainingCounts()))
                .and(MemberSpecifications.memberCourseTicketExpireDateBetween(memberSearchRequest.getTicketExpireDateFrom(), memberSearchRequest.getTicketExpireDateTo()));

        PageRequest pageRequest = PageRequest.of(memberSearchRequest.getPage() - 1, memberSearchRequest.getLimit());

        Page<Member> result = memberRepository.findAll(specification, pageRequest);
        PageableDTO.Response response = new PageableDTO.Response();
        response.setTotalElements(result.getTotalElements());
        response.setNumber(result.getNumber() + 1);
        response.setSize(result.getSize());
        response.setContent(
                result.getContent()
                        .stream()
                        .map(memberInfoMapper)
                        .toList()
        );

        return response;
    }

    public MemberDTO.MemberDetailInfo getMemberDetailInfoById(String memberId) {
        return memberRepository.findById(memberId)
                .map(memberDetailInfoMapper)
                .orElseThrow(() -> new ResourceNotFoundException(
                        messageSource.getMessage("member.id.not.found", new Object[]{memberId}, Locale.ENGLISH)));

    }

    public MemberDTO.MemberDetailAdditionInfo getMemberDetailAdditionInfoById(String memberId, Long branchId) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        messageSource.getMessage("member.id.not.found", new Object[]{memberId}, Locale.ENGLISH)));

        List<MemberCourseTicket> memberCourseTickets = member.getCourseTickets()
                .stream()
                .filter(memberCourseTicket -> memberCourseTicket.getCourseTicket().getBranch().getId().equals(branchId)
                        && memberCourseTicket.getStatus().equals(MemberCourseTicketStatus.ACTIVE))
                .toList();

        List<MemberReservation> memberReservations = member.getReservations()
                .stream()
                .filter(memberReservation -> !memberReservation.getStatus().equals(ReservationStatus.CANCELED)
                        && memberReservation.getCourseClassTime().getCourseClass().getCourseTicket().getBranch().getId().equals(branchId))
                .toList();

        MemberDTO.MemberDetailAdditionInfo memberDetailAdditionInfo = new MemberDTO.MemberDetailAdditionInfo();
        memberDetailAdditionInfo.setMemo("Memo");
        memberDetailAdditionInfo.setTrainers(memberCourseTickets.stream()
                .flatMap(memberCourseTicket -> memberCourseTicket.getCourseTicket().getCourseTrainers()
                        .stream()
                        .map(CourseTrainer::getEmployee)).toList()
                .stream()
                .map(MemberDTO.MemberTrainerInfo::new)
                .toList()
        );
        memberDetailAdditionInfo.setTicketTotalRemainingCount(memberCourseTickets
                .stream()
                .map(memberCourseTicket -> memberCourseTicket.getCourseTicket().getUsageCount() - memberCourseTicket.getUsedCount())
                .reduce(Integer::sum)
                .orElse(0)
        );
        memberDetailAdditionInfo.setTicketExpireDate(memberCourseTickets.stream()
                .min(Comparator.comparing(MemberCourseTicket::getExpireDate))
                .map(MemberCourseTicket::getExpireDate)
                .orElse(null)
        );
        memberDetailAdditionInfo.setLockerExpireDate(member.getBirthDate());
        memberDetailAdditionInfo.setTotalAttendanceCount((int) memberReservations
                .stream()
                .filter(memberReservation -> memberReservation.getStatus().equals(ReservationStatus.ATTENDED))
                .count()
        );
        memberDetailAdditionInfo.setFirstPaymentDate(member.getOrders()
                .stream()
                .filter(memberOrder -> memberOrder.getPaymentStatus().equals("PAID"))
                .min(Comparator.comparing(MemberOrder::getPaidAt))
                .map(MemberOrder::getPaidAt)
                .orElse(null)
        );
        memberDetailAdditionInfo.setLockerExpireDate(member.getBirthDate());
        memberDetailAdditionInfo.setReservations(memberReservations
                .stream()
                .filter(memberReservation -> memberReservation.getStatus().equals(ReservationStatus.RESERVED))
                .sorted(Comparator.comparing(MemberReservation::getRegisteredDate, Comparator.reverseOrder()))
                .limit(3)
                .map(memberReservationInfoMapper)
                .toList()
        );
        memberDetailAdditionInfo.setActiveTickets(memberCourseTickets
                .stream()
                .sorted(Comparator.comparing(MemberCourseTicket::getRegisteredDate, Comparator.reverseOrder()))
                .limit(3)
                .map(memberActiveTicketInfoMapper)
                .toList()
        );

        return memberDetailAdditionInfo;
    }
}

