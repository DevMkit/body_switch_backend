package kr.co.softhubglobal.dto;

import lombok.*;

import java.util.List;

public class PageableDTO {

    @Data
    public static class Request {
        private int page = 1;
        private int limit = 10;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private Long totalElements;
        private Integer size;
        private Integer number;
        private List<?> content;
    }
}

