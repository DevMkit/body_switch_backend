package kr.co.softhubglobal.dto.member;

import lombok.AllArgsConstructor;
import lombok.Data;

public class MemberDTO {

    @Data
    @AllArgsConstructor
    public static class MemberInfo {

        private String username;
        private String name;
        private String gender;
        private int age;
    }
}
