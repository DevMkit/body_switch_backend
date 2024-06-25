package kr.co.softhubglobal.service.member;

import kr.co.softhubglobal.dto.PageableDTO;
import kr.co.softhubglobal.dto.member.MemberDTO;
import kr.co.softhubglobal.dto.member.MemberInfoMapper;
import kr.co.softhubglobal.entity.employee.Employee;
import kr.co.softhubglobal.entity.member.Member;
import kr.co.softhubglobal.repository.MemberRepository;
import kr.co.softhubglobal.service.employee.EmployeeSpecifications;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final MemberInfoMapper memberInfoMapper;

    public PageableDTO.Response getAllMembers(MemberDTO.MemberSearchRequest memberSearchRequest) {

        Specification<Member> specification = Specification
                .where(MemberSpecifications.memberCourseTicketBranchIdEqual(memberSearchRequest.getBranchId()))
                .and(MemberSpecifications.memberNameOrUsernameOrPhoneNumberLike(memberSearchRequest.getSearchInput()))
                .and(MemberSpecifications.memberCourseTicketTypeIn(memberSearchRequest.getClassTypes()))
                .and(MemberSpecifications.memberCourseTicketIdEqual(memberSearchRequest.getCourseTicketId()))
                .and(MemberSpecifications.memberCourseTicketTrainerIdEqual(memberSearchRequest.getCourseTrainerId()))
                .and(MemberSpecifications.memberGenderIn(memberSearchRequest.getGenders()));

        PageRequest pageRequest = PageRequest.of(
                memberSearchRequest.getPage() - 1,
                memberSearchRequest.getLimit()
        );

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
}
