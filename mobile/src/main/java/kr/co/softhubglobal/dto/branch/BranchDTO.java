package kr.co.softhubglobal.dto.branch;

import lombok.AllArgsConstructor;
import lombok.Data;

public class BranchDTO {

    @Data
    @AllArgsConstructor
    public static class BranchInfo {

        private Long id;
        private String branchName;
    }
}
