package kr.hhplus.be.server.domain.reservation;

import kr.hhplus.be.server.common.exception.ReservationNotFoundException;
import kr.hhplus.be.server.domain.seat.Seat;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReservationService {
    private final ReservationRepository reservationRepository;

    @Transactional
    public Reservation creatSeatReservation(Seat seat, Long userId) {
       Reservation createdReservation =  new Reservation().create(seat, userId);
       return reservationRepository.save(createdReservation);
    }

    public Reservation findByReservationIdAndSeatId(Long reservationId, Long seatId) {

        Reservation findReservation = reservationRepository.findByReservationIdAndSeatId(reservationId, seatId)
                .orElseThrow(()-> new ReservationNotFoundException("예약 정보를 찾을 수 없습니다."));

        return findReservation;
    }

    @Transactional
    public Reservation updateSeatReservation(Seat seat, Long reservationId, Long userId, ReservationState reservationState) {
        Reservation updatedReservation = new Reservation().update(seat,reservationId, userId, reservationState);
        return reservationRepository.save(updatedReservation);
    }
}
