package kr.co.softhubglobal.dto.member;

import kr.co.softhubglobal.entity.member.MemberCourseTicket;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class MemberActiveTicketInfoMapper implements Function<MemberCourseTicket, MemberDTO.MemberActiveTicketInfo> {

    @Override
    public MemberDTO.MemberActiveTicketInfo apply(MemberCourseTicket memberCourseTicket) {
        return new MemberDTO.MemberActiveTicketInfo(
                memberCourseTicket.getCourseTicket().getClassType(),
                memberCourseTicket.getCourseTicket().getTicketName(),
                memberCourseTicket.getExpireDate(),
                memberCourseTicket.getCourseTicket().usageCount - memberCourseTicket.getUsedCount()
        );
    }
}
