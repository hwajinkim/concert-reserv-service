package kr.hhplus.be.server.interfaces.api.dto.reservation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public record ReservationRequest(
        Long scheduleId,
        Long seatId,
        Long userId     //유저 대기열 토큰에서 받을 값.
) {

    public static ReservationRequest withUserId(Long tokenUserId, ReservationRequest reservationRequest) {
        return new ReservationRequest(reservationRequest.scheduleId(), reservationRequest.seatId(), tokenUserId);
    }
}
