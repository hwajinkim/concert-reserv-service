package kr.hhplus.be.server.unit_test.domain.service;

import kr.hhplus.be.server.common.exception.AvailableSeatNotFoundException;
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
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SeatServiceTest {

    @InjectMocks
    private SeatService seatService;

    @Mock
    private SeatRepository seatRepository;

    @Test
    void 스케줄ID로_조회_시_예약_가능한_좌석_정보가_없으면_AvailableSeatNotFoundException_발생(){
        //given
        Long scheduleId = 999L;
        when(seatRepository.findAvailableSeatsByScheduleId(scheduleId)).thenReturn(null);

        //when & then
        Exception exception = assertThrows(AvailableSeatNotFoundException.class,
                ()-> seatService.getAvailableSeats(scheduleId));

        assertEquals("예약 가능한 좌석 정보를 찾을 수 없습니다.", exception.getMessage());
    }

    @Test
    void 스케줄ID로_조회_시_예약_가능한_좌석_정보가_있으면_비점유_상태인_좌석정보_반환(){
        //given
        Long scheduleId = 12345L;

        List<Seat> mockSeats = List.of(
                Seat.builder()
                        .seatId(1L)
                        .build(),
                Seat.builder()
                        .seatId(2L)
                        .build(),
                Seat.builder()
                        .seatId(3L)
                        .build());

        when(seatRepository.findAvailableSeatsByScheduleId(scheduleId)).thenReturn(mockSeats);
        //when
        List<Seat> availableSeats = seatService.getAvailableSeats(scheduleId);

        //then
        assertEquals(availableSeats, mockSeats);
    }


}
