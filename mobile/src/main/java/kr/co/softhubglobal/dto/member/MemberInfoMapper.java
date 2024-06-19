package kr.co.softhubglobal.dto.member;

import kr.co.softhubglobal.entity.member.Member;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class MemberInfoMapper implements Function<Member, MemberDTO.MemberInfo> {

    @Override
    public MemberDTO.MemberInfo apply(Member member) {

        return new MemberDTO.MemberInfo(
                member.getId(),
                member.getUser().getName(),
                member.getProfileImage()
        );
    }
}
