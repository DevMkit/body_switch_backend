package kr.co.softhubglobal.dto.searchOption;

import kr.co.softhubglobal.entity.center.Center;
import lombok.Data;

public class SearchOptionDTO {

    @Data
    public static class HeadBranchInfo {

        private Long headBranchId;
        private String headBranchName;

        public HeadBranchInfo(final Center center) {
            this.headBranchId = center.getId();
            this.headBranchName = center.getBranch().getBranchName();
        }
    }

    @Data
    public static class BranchInfo {

        private Long branchId;
        private String branchName;

        public BranchInfo(final Center center) {
            this.branchId = center.getBranch().getId();
            this.branchName = center.getBranch().getBranchName();
        }
    }
}
