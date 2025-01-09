package kr.hhplus.be.server.application.dto.reservation;

import kr.hhplus.be.server.domain.reservation.ReservationState;

import java.time.LocalDateTime;

public record ReservationResult(
        Long reservationId,
        Long scheduleId,
        Long seatId,
        Long userId,
        ReservationState reservationState,

        LocalDateTime createdAt
) {
}
