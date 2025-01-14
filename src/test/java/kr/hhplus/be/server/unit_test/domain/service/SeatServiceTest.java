package kr.hhplus.be.server.unit_test.domain.service;

import kr.hhplus.be.server.common.exception.AvailableSeatNotFoundException;
import kr.hhplus.be.server.common.exception.SeatNotFoundException;
import kr.hhplus.be.server.domain.concert.Schedule;
import kr.hhplus.be.server.domain.seat.Seat;
import kr.hhplus.be.server.domain.seat.SeatRepository;
import kr.hhplus.be.server.domain.seat.SeatService;
import kr.hhplus.be.server.domain.seat.SeatStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SeatServiceTest {

    @InjectMocks
    private SeatService seatService;

    @Mock
    private SeatRepository seatRepository;

    @Test
    void 좌석_예약_시_좌석ID에_해당하는_좌석이_없으면_SeatNotFoundException_발생(){
        //given
        Long seatId = 999L;
        //when & then
        Exception exception = assertThrows(SeatNotFoundException.class,
                ()-> seatService.updateSeatStatus(seatId, SeatStatus.OCCUPIED));

        assertEquals("좌석을 찾을 수 없습니다.", exception.getMessage());
    }

    @Test
    void 좌석_예약_시_좌석ID에_해당하는_좌석이_있으면_좌석상태_변경_후_Seat_반환(){
        //given
        Long seatId = 1L;

        Seat mockSeat = Seat.builder()
                .seatId(seatId)
                .seatNumber(1)
                .seatStatus(SeatStatus.AVAILABLE)
                .seatPrice(BigDecimal.valueOf(10000.00))
                .schedule(null)
                .build();

        Seat updatedMockSeat = Seat.builder()
                .seatId(seatId)
                .seatNumber(1)
                .seatStatus(SeatStatus.OCCUPIED)
                .seatPrice(BigDecimal.valueOf(10000.00))
                .schedule(null)
                .build();

        when(seatRepository.findByIdWithLock(seatId)).thenReturn(Optional.of(mockSeat));
        when(seatRepository.save(any(Seat.class))).thenReturn(updatedMockSeat);
        //when
        Seat updatedSeat = seatService.updateSeatStatus(seatId, SeatStatus.OCCUPIED);
        //then
        assertEquals(SeatStatus.OCCUPIED, updatedSeat.getSeatStatus());
        verify(seatRepository).save(any(Seat.class));
    }
}
