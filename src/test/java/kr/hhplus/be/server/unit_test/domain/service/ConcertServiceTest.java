package kr.hhplus.be.server.unit_test.domain.service;

import kr.hhplus.be.server.common.exception.ConcertScheduleNotFoundException;
import kr.hhplus.be.server.domain.concert.Concert;
import kr.hhplus.be.server.domain.concert.ConcertRepository;
import kr.hhplus.be.server.domain.concert.ConcertService;
import kr.hhplus.be.server.domain.concert.Schedule;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ConcertServiceTest {

    @InjectMocks
    private ConcertService concertService;

    @Mock
    private ConcertRepository concertRepository;


    @Test
    void 콘서트_ID가_NULL_이면_IllegalArgumentException_발생(){
        //given
        Long concertId = null;

        when(concertRepository.findByConcertWithSchedule(concertId)).thenThrow(new IllegalArgumentException("콘서트 ID 유효하지 않음."));

        //when & then
        Exception exception = assertThrows(IllegalArgumentException.class,
                ()-> concertService.findByConcertWithSchedule(concertId));

        assertEquals("콘서트 ID 유효하지 않음.", exception.getMessage());
    }

    @Test
    void 콘서트_ID로_조회_시_콘서트_스케줄_정보가_없으면_ConcertScheduleNotFoundException_발생(){
        //given
        Long concertId = 999L;

        when(concertRepository.findByConcertWithSchedule(concertId)).thenReturn(Optional.empty());

        //when & then
        Exception exception = assertThrows(ConcertScheduleNotFoundException.class,
                ()-> concertService.findByConcertWithSchedule(concertId));

        assertEquals("콘서트 스케줄 정보를 찾을 수 없습니다.", exception.getMessage());
    }

    @Test
    void 콘서트_ID로_조회_시_콘서트_스케줄_정보가_있으면_Concert_반환(){
        //given
        Long concertId = 12345L;

        Schedule mockScheduleFirst = Schedule.builder()
                .scheduleId(67890L)
                .concertDateTime(LocalDateTime.of(2025,1,15,20,0,0))
                .bookingStart(LocalDateTime.of(2025,1,1,10,0,0))
                .bookingEnd(LocalDateTime.of(2025,1,10,18,0,0))
                .remainingTicket(50)
                .build();

        Schedule mockScheduleSecond = Schedule.builder()
                .scheduleId(67891L)
                .concertDateTime(LocalDateTime.of(2025,1,20,22,0,0))
                .bookingStart(LocalDateTime.of(2025,1,5,10,0,0))
                .bookingEnd(LocalDateTime.of(2025,1,15,18,0,0))
                .remainingTicket(30)
                .build();
        List<Schedule> mockSchedules = new ArrayList<>();
        mockSchedules.add(mockScheduleFirst);
        mockSchedules.add(mockScheduleSecond);

        Concert mockConcert = Concert.builder()
                .concertId(concertId)
                .concertName("Awesome Concert")
                .schedules(mockSchedules)
                .build();

        when(concertRepository.findByConcertWithSchedule(concertId)).thenReturn(Optional.of(mockConcert));
        //when
        Concert findConcertSchedule = concertService.findByConcertWithSchedule(concertId);

        //then
        assertEquals(findConcertSchedule, mockConcert);
        verify(concertRepository,times(1)).findByConcertWithSchedule(concertId);

    }
}
