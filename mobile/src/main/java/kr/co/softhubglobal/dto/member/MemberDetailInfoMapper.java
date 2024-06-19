package kr.co.softhubglobal.dto.member;

import kr.co.softhubglobal.entity.member.Member;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.function.Function;

@Service
public class MemberDetailInfoMapper implements Function<Member, MemberDTO.MemberDetailInfo> {

    @Override
    public MemberDTO.MemberDetailInfo apply(Member member) {
        return new MemberDTO.MemberDetailInfo(
                member.getId(),
                member.getProfileImage(),
                member.getUser().getUsername(),
                member.getUser().getName(),
                member.getUser().getPhoneNumber(),
                member.getBirthDate(),
                member.getGender(),
                member.getUser().getEmail(),
                member.getPostalCode(),
                member.getAddress(),
                member.getAddressDetail()
        );
    }
}
