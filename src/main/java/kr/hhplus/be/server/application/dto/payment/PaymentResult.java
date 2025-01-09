package kr.hhplus.be.server.application.dto.payment;

import kr.hhplus.be.server.domain.payment.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PaymentResult(
        Long paymentId,
        Long reservationId,
        Long seatId,
        LocalDateTime concertDateTime,
        BigDecimal paymentAmount,
        PaymentStatus paymentStatus,
        LocalDateTime paymentTime
) {
}
