package kr.hhplus.be.server.interfaces.api.dto.payment;

public record PaymentRequest(
        Long reservationId,
        Long seatId,
        Long userId, // 유저 대기열 토큰에서 읽어올 값.

        String queueId
) {
    public PaymentRequest withUserIdAndQueueId(Long tokenUserId, String tokenQueueId, PaymentRequest paymentRequest) {
        return new PaymentRequest(paymentRequest.reservationId(), paymentRequest.seatId(), tokenUserId, tokenQueueId);
    }
}
