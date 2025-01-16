package kr.hhplus.be.server.domain.reservation;

import kr.hhplus.be.server.common.exception.MissingExpiryTimeException;
import kr.hhplus.be.server.common.exception.ReservationExpiredException;
import kr.hhplus.be.server.common.exception.ReservationNotFoundException;
import kr.hhplus.be.server.domain.dto.ReservationCheckResult;
import kr.hhplus.be.server.domain.concert.Seat;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReservationService {
    private final ReservationRepository reservationRepository;

    public Reservation creatSeatReservation(Seat seat, Long userId) {
        Reservation createdReservation =  new Reservation().create(seat, userId);
        return reservationRepository.save(createdReservation);
    }

    public Reservation findByReservationIdAndSeatId(Long reservationId, Long seatId) {
         return reservationRepository.findByReservationIdAndSeatId(reservationId, seatId)
                .orElseThrow(()-> new ReservationNotFoundException("예약 정보를 찾을 수 없습니다."));
    }

    public ReservationCheckResult checkReservationExpiration(Long reservationId, Long seatId) {
        Reservation reservation = reservationRepository.findByReservationIdAndSeatId(reservationId, seatId)
                .orElseThrow(()-> new ReservationNotFoundException("예약 정보를 찾을 수 없습니다."));

        Reservation updatedReservation = null;
        boolean result = false;
        if(reservation.getExpiredAt() != null){
            //임시 예약 만료 되었을 때
            if(reservation.getExpiredAt().isBefore(LocalDateTime.now())){
                // 예약 상태 "CANCELLED"로 변경
                updatedReservation = new Reservation().update(reservation.getId(), reservation.getSeatId(), reservation.getUserId(), ReservationState.CANCELLED, reservation.getSeatPrice());
                result = true;
            } else {  //임시 예약 만료 안 되었을 때
                // 예약 상태 "PAID"로 변경
                updatedReservation = new Reservation().update(reservation.getId(), reservation.getSeatId(), reservation.getUserId(), ReservationState.PAID, reservation.getSeatPrice());
                result = false;
            }
        } else {
            throw new MissingExpiryTimeException("예약에 만료 시간이 설정되지 않았습니다.");
        }
        return new ReservationCheckResult(result, reservationRepository.save(updatedReservation));
    }
}
