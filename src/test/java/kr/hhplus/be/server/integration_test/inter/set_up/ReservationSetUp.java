package kr.hhplus.be.server.integration_test.inter.set_up;

import kr.hhplus.be.server.domain.reservation.Reservation;
import kr.hhplus.be.server.domain.reservation.ReservationState;
import kr.hhplus.be.server.infrastructure.reservation.ReservationJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Component
public class ReservationSetUp {

    @Autowired
    private ReservationJpaRepository reservationJpaRepository;

    public Reservation saveReservation(Long userId, Long seatId, ReservationState reservationState, BigDecimal seatPrice, LocalDateTime expiredAt) {
        Reservation reservation = Reservation.builder()
                .userId(userId)
                .seatId(seatId)
                .reservationState(reservationState)
                .seatPrice(seatPrice)
                .expiredAt(expiredAt)
                .build();

        return reservationJpaRepository.save(reservation);
    }
}
