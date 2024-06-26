package kr.co.softhubglobal.service.member;

import kr.co.softhubglobal.dto.PageableDTO;
import kr.co.softhubglobal.dto.member.MemberDTO;
import kr.co.softhubglobal.dto.member.MemberDetailInfoMapper;
import kr.co.softhubglobal.dto.member.MemberInfoMapper;
import kr.co.softhubglobal.entity.member.Member;
import kr.co.softhubglobal.exception.customExceptions.DuplicateResourceException;
import kr.co.softhubglobal.exception.customExceptions.ResourceNotFoundException;
import kr.co.softhubglobal.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final MemberInfoMapper memberInfoMapper;
    private final MemberDetailInfoMapper memberDetailInfoMapper;
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
                .and(MemberSpecifications.memberIsSmsReceive(memberSearchRequest.getIsSMSReceive()))
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
                        messageSource.getMessage("member.user.id.not.found", new Object[]{memberId}, Locale.ENGLISH)));

    }
}
