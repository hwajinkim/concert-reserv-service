package kr.hhplus.be.server.unit_test.domain.application;

import kr.hhplus.be.server.application.dto.payment.PaymentParam;
import kr.hhplus.be.server.application.payment.PaymentFacade;
import kr.hhplus.be.server.common.exception.MissingExpiryTimeException;
import kr.hhplus.be.server.common.exception.ReservationExpiredException;
import kr.hhplus.be.server.domain.concert.ConcertService;
import kr.hhplus.be.server.domain.concert.Schedule;
import kr.hhplus.be.server.domain.dto.ReservationCheckResult;
import kr.hhplus.be.server.domain.payment.PaymentService;
import kr.hhplus.be.server.domain.reservation.Reservation;
import kr.hhplus.be.server.domain.reservation.ReservationService;
import kr.hhplus.be.server.domain.reservation.ReservationState;
import kr.hhplus.be.server.domain.seat.Seat;
import kr.hhplus.be.server.domain.seat.SeatService;
import kr.hhplus.be.server.domain.seat.SeatStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PaymentFacadeTest {

    @InjectMocks
    private PaymentFacade paymentFacade;

    @Mock
    private ReservationService reservationService;

    @Mock
    private SeatService seatService;

    @Mock
    private ConcertService concertService;
    @Test
    void 결제_시_예약_정보의_만료_시간이_null이면_MissingExpiryTimeException_발생(){
        //given
        Long reservationId = 1L;
        Long seatId = 1L;
        Long userId = 12345L;
        Long queueId = 1L;

        LocalDateTime expiryTime = LocalDateTime.now().plusMinutes(5);

        Reservation mockReservation = Reservation.builder()
                .userId(userId)
                .seatId(seatId)
                .reservationState(ReservationState.PANDING)
                .seatPrice(BigDecimal.valueOf(10000.00))
                .expiredAt(null)
                .build();

        PaymentParam paymentParam = new PaymentParam(reservationId, seatId, userId, queueId);
        when(reservationService.findByReservationIdAndSeatId(paymentParam.reservationId(), paymentParam.seatId()))
                .thenReturn(mockReservation);

        //when
        Exception exception = assertThrows(MissingExpiryTimeException.class,
                ()-> paymentFacade.createPayment(paymentParam));

        // then
        assertEquals("예약에 만료 시간이 설정되지 않았습니다.", exception.getMessage());
        verify(reservationService, times(1)).findByReservationIdAndSeatId(reservationId, seatId);
    }

    @Test
    void 결제_시_임시_예약이_만료_되었으면_상태_변경_후_ReservationExpiredException_발생(){
        Long reservationId = 1L;
        Long seatId = 1L;
        Long userId = 2L;
        Long queueId = 1L;
        ReservationState reservationState = ReservationState.CANCELLED;

        PaymentParam paymentParam = new PaymentParam(reservationId, seatId, userId, queueId);

        Reservation expiredReservation = Reservation.builder()
                .expiredAt(LocalDateTime.now().minusMinutes(5))
                .seatId(seatId)
                .build();

        Seat updatedSeat = Seat.builder()
                .seatId(seatId)
                .seatStatus(SeatStatus.AVAILABLE)
                .build();

        Schedule updatedSchedule = Schedule.builder()
                .scheduleId(1L)
                .remainingTicket(101)
                .build();

        ReservationCheckResult reservationCheckResult = new ReservationCheckResult(false, expiredReservation);

        when(reservationService.checkReservationExpiration(reservationId, seatId)).thenReturn(reservationCheckResult);
        when(concertService.updateSeatStatus(seatId, SeatStatus.AVAILABLE)).thenReturn(updatedSeat);
        when(concertService.updateScheduleRemainingTicket(1L, 1)).thenReturn(updatedSchedule);

        //when
        Exception exception = assertThrows(ReservationExpiredException.class,
                () -> paymentFacade.createPayment(paymentParam));

        assertEquals("예약이 만료되었습니다.", exception.getMessage());
    }
}
