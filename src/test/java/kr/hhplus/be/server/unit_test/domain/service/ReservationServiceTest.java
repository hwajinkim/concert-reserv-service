package kr.hhplus.be.server.unit_test.domain.service;

import kr.hhplus.be.server.domain.reservation.Reservation;
import kr.hhplus.be.server.domain.reservation.ReservationRepository;
import kr.hhplus.be.server.domain.reservation.ReservationService;
import kr.hhplus.be.server.domain.reservation.ReservationState;
import kr.hhplus.be.server.domain.seat.Seat;
import kr.hhplus.be.server.domain.seat.SeatStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ReservationServiceTest {

    @InjectMocks
    private ReservationService reservationService;

    @Mock
    private ReservationRepository reservationRepository;

    @Test
    void 좌석_예약_신청_완료_시_예약정보_저장_후_Reservation_반환(){
        //given
        Long userId = 1L;

        Seat mockSeat = Seat.builder()
                .seatId(1L)
                .seatNumber(1)
                .seatStatus(SeatStatus.OCCUPIED)
                .seatPrice(BigDecimal.valueOf(10000.00))
                .schedule(null)
                .build();

        LocalDateTime expiryTime = LocalDateTime.now().plusMinutes(5);

        Reservation mockReservation = Reservation.builder()
                .userId(userId)
                .seatId(mockSeat.getId())
                .reservationState(ReservationState.PANDING)
                .seatPrice(mockSeat.getSeatPrice())
                .expiredAt(expiryTime)
                .build();

        when(reservationRepository.save(any(Reservation.class))).thenReturn(mockReservation);
        //when
        Reservation savedReservation = reservationService.creatSeatReservation(mockSeat, userId);
        //then
        assertEquals(savedReservation, mockReservation);

    }
}
