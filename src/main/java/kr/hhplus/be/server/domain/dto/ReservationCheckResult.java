package kr.hhplus.be.server.domain.dto;

import kr.hhplus.be.server.domain.reservation.Reservation;

public record ReservationCheckResult(
        boolean isExpired,
        Reservation reservation
) {
}
