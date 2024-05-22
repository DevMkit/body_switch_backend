package kr.co.softhubglobal.service;

import kr.co.softhubglobal.dto.member.MemberDTO;
import kr.co.softhubglobal.dto.member.MemberInfoMapper;
import kr.co.softhubglobal.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final MemberInfoMapper memberInfoMapper;

    public List<MemberDTO.MemberInfo> getAllMembers() {
        return memberRepository.findAll()
                .stream()
                .map(memberInfoMapper)
                .toList();
    }
}
