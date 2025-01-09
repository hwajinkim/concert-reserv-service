package kr.hhplus.be.server.application.dto.payment;

import kr.hhplus.be.server.interfaces.api.dto.payment.PaymentRequest;

public record PaymentParam(
        Long reservationId,
        Long seatId,
        Long userId // 유저 대기열 토큰에서 읽어올 값.
) {
    public static PaymentParam from(PaymentRequest paymentRequest){
        return new PaymentParam(paymentRequest.reservationId(), paymentRequest.seatId(), paymentRequest.userId());
    }
}
