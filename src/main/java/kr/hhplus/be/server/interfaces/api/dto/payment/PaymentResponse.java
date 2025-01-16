package kr.hhplus.be.server.interfaces.api.dto.payment;

import kr.hhplus.be.server.application.dto.payment.PaymentResult;
import kr.hhplus.be.server.domain.payment.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PaymentResponse(
        Long paymentId,
        Long reservationId,
        Long seatId,
        LocalDateTime concertDateTime,
        BigDecimal paymentAmount,
        PaymentStatus paymentStatus,
        LocalDateTime paymentTime
) {

    public static PaymentResponse from(PaymentResult paymentResult) {
        return new PaymentResponse(paymentResult.paymentId(), paymentResult.reservationId(), paymentResult.seatId(),
                paymentResult.concertDateTime(), paymentResult.paymentAmount(), paymentResult.paymentStatus(), paymentResult.paymentTime());
    }
}
