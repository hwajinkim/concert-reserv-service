package kr.hhplus.be.server.unit_test.domain.service;

import kr.hhplus.be.server.common.exception.ReservationNotFoundException;
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

    @Test
    void 결제_요청_시_예약ID_좌석_ID로_조회_시_예약_정보가_없으면_ReservationNotFoundException_발생(){
        //given
        Long reservationId = 1L;
        Long seatId = 1L;
        when(reservationRepository.findByReservationIdAndSeatId(reservationId, seatId)).thenThrow(new ReservationNotFoundException("예약 정보를 찾을 수 없습니다."));

        //when
        Exception exception = assertThrows(ReservationNotFoundException.class,
                ()-> reservationService.findByReservationIdAndSeatId(reservationId, seatId));

        //then
        assertEquals("예약 정보를 찾을 수 없습니다.", exception.getMessage());
    }

    @Test
    void 결제_요청_시_임시예약_시간이_만료_되었을_때_예약_상태_CANCELLED로_업데이트_되었는지_확인(){
        //given
        Long userId = 1L;
        Long reservationId = 1L;
        Seat mockSeat = Seat.builder()
                .seatId(1L)
                .seatNumber(1)
                .seatStatus(SeatStatus.OCCUPIED)
                .seatPrice(BigDecimal.valueOf(10000.00))
                .schedule(null)
                .build();

        ReservationState reservationState = ReservationState.CANCELLED;

        Reservation updatedReservation = Reservation.builder()
                .userId(userId)
                .seatId(mockSeat.getId())
                .reservationState(ReservationState.CANCELLED)
                .seatPrice(mockSeat.getSeatPrice())
                .expiredAt(null)
                .build();

        when(reservationRepository.save(any(Reservation.class))).thenReturn(updatedReservation);
        //when
        Reservation reservation = reservationService.updateSeatReservation(mockSeat, reservationId, userId, reservationState);
        //then
        assertEquals(ReservationState.CANCELLED, reservation.getReservationState());
    }

    @Test
    void 결제_요청_시_임시예약_시간이_만료_되지_않았을_때_예약_상태_PAID로_업데이트_되었는지_확인(){
        //given
        Long userId = 1L;
        Long reservationId = 1L;
        Seat mockSeat = Seat.builder()
                .seatId(1L)
                .seatNumber(1)
                .seatStatus(SeatStatus.OCCUPIED)
                .seatPrice(BigDecimal.valueOf(10000.00))
                .schedule(null)
                .build();

        ReservationState reservationState = ReservationState.PAID;

        Reservation updatedReservation = Reservation.builder()
                .userId(userId)
                .seatId(mockSeat.getId())
                .reservationState(ReservationState.PAID)
                .seatPrice(mockSeat.getSeatPrice())
                .expiredAt(null)
                .build();

        when(reservationRepository.save(any(Reservation.class))).thenReturn(updatedReservation);
        //when
        Reservation reservation = reservationService.updateSeatReservation(mockSeat, reservationId, userId, reservationState);
        //then
        assertEquals(ReservationState.PAID, reservation.getReservationState());
    }
}
