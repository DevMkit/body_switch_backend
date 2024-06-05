package kr.co.softhubglobal.dto.nicepay;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDate;

public class NicepayDTO {

    @Data
    public static class PaymentCreateRequest {

        private Long ticketId;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        private LocalDate courseStartDate;
    }
}
