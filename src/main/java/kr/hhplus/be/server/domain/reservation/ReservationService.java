package kr.hhplus.be.server.domain.reservation;

import kr.hhplus.be.server.domain.seat.Seat;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReservationService {
    private final ReservationRepository reservationRepository;

    @Transactional
    public Reservation creatSeatReservation(Seat seat, Long userId) {
       Reservation createdReservation =  new Reservation().create(seat, userId);
       return reservationRepository.save(createdReservation);
    }
}
