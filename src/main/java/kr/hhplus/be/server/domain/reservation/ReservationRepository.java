package kr.hhplus.be.server.domain.reservation;

import java.util.Optional;

public interface ReservationRepository {

    Reservation save(Reservation createdReservation);

    Optional<Reservation> findByReservationIdAndSeatId(Long reservationId, Long seatId);
}
