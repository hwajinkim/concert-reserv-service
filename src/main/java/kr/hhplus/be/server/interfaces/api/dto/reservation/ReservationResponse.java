package kr.hhplus.be.server.interfaces.api.dto.reservation;

import kr.hhplus.be.server.application.dto.reservation.ReservationResult;
import kr.hhplus.be.server.domain.reservation.ReservationState;
import java.time.LocalDateTime;

public record ReservationResponse (
        Long reservationId,
        Long scheduleId,
        Long seatId,
        Long userId,
        ReservationState state,
        LocalDateTime createAt
){


    public static ReservationResponse from(ReservationResult reservationResult) {
        return new ReservationResponse(reservationResult.reservationId(), reservationResult.scheduleId(), reservationResult.seatId(), reservationResult.userId(),
                reservationResult.reservationState(), reservationResult.createdAt());
    }
}
