package kr.co.softhubglobal.dto.member;

import kr.co.softhubglobal.entity.member.Member;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class MemberDetailInfoMapper implements Function<Member, MemberDTO.MemberDetailInfo> {

    @Override
    public MemberDTO.MemberDetailInfo apply(Member member) {
        return new MemberDTO.MemberDetailInfo(
                member.getId(),
                member.getProfileImage(),
                MemberDTO.MemberType.ACTIVE,
                member.getUser().getName(),
                member.getUser().getUsername(),
                member.getUser().getPhoneNumber(),
                member.getBirthDate(),
                member.getAge(),
                member.getUser().getEmail(),
                member.getGender(),
                member.isSMSReceive(),
                member.getAddress(),
                member.getAddressDetail(),
                member.getRegisteredDate()
        );
    }
}
