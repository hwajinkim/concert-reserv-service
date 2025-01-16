package kr.hhplus.be.server.domain.reservation;

import kr.hhplus.be.server.common.exception.ReservationBadStatusException;
import kr.hhplus.be.server.common.exception.ReservationNotFoundException;
import kr.hhplus.be.server.domain.concert.Seat;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReservationService {
    private final ReservationRepository reservationRepository;

    public Reservation creatSeatReservation(Seat seat, Long userId) {
        Reservation createdReservation =  new Reservation().create(seat, userId);
        return reservationRepository.save(createdReservation);
    }

    public Reservation updateReservation(Long reservationId, Long seatId) {
         Reservation reservation = reservationRepository.findByReservationIdAndSeatId(reservationId, seatId)
                .orElseThrow(()-> new ReservationNotFoundException("예약 정보를 찾을 수 없습니다."));

         if(reservation.getReservationState().equals(ReservationState.PANDING)){
             Reservation updateReservation = new Reservation().update(reservation.getId(), reservation.getSeatId(), reservation.getUserId(), ReservationState.PAID, reservation.getSeatPrice());
             reservationRepository.save(updateReservation);
         } else {
             throw new ReservationBadStatusException("유효하지 않은 예약 상태입니다.");
         }
         return reservation;
    }

    public List<Reservation> checkReservationExpiration() {
        List<Reservation> expiredReservations = reservationRepository.findExpiredReservation(LocalDateTime.now());

        if(expiredReservations != null && !expiredReservations.isEmpty()){
            Reservation updatedReservation = null;
            for (Reservation reservation : expiredReservations) {
                updatedReservation = new Reservation().update(reservation.getId(), reservation.getSeatId(), reservation.getUserId(), ReservationState.CANCELLED, reservation.getSeatPrice());
            }
            reservationRepository.save(updatedReservation);
        }
        return expiredReservations;
    }
}
