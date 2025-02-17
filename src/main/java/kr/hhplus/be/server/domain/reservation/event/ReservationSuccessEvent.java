package kr.hhplus.be.server.domain.reservation.event;

import kr.hhplus.be.server.domain.reservation.Reservation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ReservationSuccessEvent {
    private final Long reservationId;
}
