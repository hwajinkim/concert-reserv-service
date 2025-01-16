package kr.hhplus.be.server.integration_test.application;

import kr.hhplus.be.server.common.exception.AvailableSeatNotFoundException;
import kr.hhplus.be.server.common.exception.ConcertScheduleNotFoundException;
import kr.hhplus.be.server.domain.concert.Concert;
import kr.hhplus.be.server.domain.concert.ConcertService;
import kr.hhplus.be.server.domain.concert.Schedule;
import kr.hhplus.be.server.domain.concert.Seat;
import kr.hhplus.be.server.domain.concert.SeatStatus;
import kr.hhplus.be.server.integration_test.application.set_up.ConcertSetUp;
import kr.hhplus.be.server.integration_test.application.set_up.ScheduleSetUp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ConcertServiceIntegrationTest extends BaseIntegrationTest{

    @Autowired
    private ConcertSetUp concertSetUp;

    @Autowired
    private ScheduleSetUp scheduleSetUp;

    @Autowired
    private ConcertService concertService;

    private List<Schedule> scheduleList;

    private List<Seat> seatList;

    @BeforeEach
    void setUp(){
        scheduleList = List.of(
                Schedule.builder()
                        .price(BigDecimal.valueOf(10000.00))
                        .concertDateTime(LocalDateTime.of(2025,1,15,20,0,0))
                        .bookingStart(LocalDateTime.of(2025,1,1, 10,0,0))
                        .bookingEnd(LocalDateTime.of(2025,1,10,18,0,0))
                        .remainingTicket(50)
                        .totalTicket(300)
                        .build(),
                Schedule.builder()
                        .price(BigDecimal.valueOf(15000.00))
                        .concertDateTime(LocalDateTime.of(2025,1,20,18,0,0))
                        .bookingStart(LocalDateTime.of(2025,1,5, 10,0,0))
                        .bookingEnd(LocalDateTime.of(2025,1,14,18,0,0))
                        .remainingTicket(30)
                        .totalTicket(300)
                        .build()
        );
        seatList = List.of(
                Seat.builder()
                        .seatNumber(1)
                        .seatStatus(SeatStatus.AVAILABLE)
                        .seatPrice(BigDecimal.valueOf(10000.00))
                        .build(),
                Seat.builder()
                        .seatNumber(2)
                        .seatStatus(SeatStatus.AVAILABLE)
                        .seatPrice(BigDecimal.valueOf(15000.00))
                        .build()
        );
    }

    @Test
    void 예약_가능한_콘서트_스케줄_날짜_조회(){
        //given
        Concert concert = concertSetUp.saveConcert("Awesome Concert", scheduleList);

        //when
        Concert findConcert = concertService.findByConcertWithSchedule(concert.getId());

        //then
        assertEquals(findConcert.getConcertName(), concert.getConcertName());
        assertEquals(findConcert.getSchedules().get(0).getConcertDateTime(), concert.getSchedules().get(0).getConcertDateTime());
    }

    @Test
    void 예약_가능한_콘서트_스케줄_날짜_조회_시_가능한_스케줄_존재하지_않으면_ConcertScheduleNotFoundException_발생(){
        //given
        //when & then
        Exception exception = assertThrows(ConcertScheduleNotFoundException.class,
                ()->concertService.findByConcertWithSchedule(999L));

        assertEquals("콘서트 스케줄 정보를 찾을 수 없습니다.", exception.getMessage());
    }

    @Test
    void 예약_가능한_좌석_조회(){
        //given
        Concert concert = concertSetUp.saveConcert("Awesome Concert", scheduleList);
        Schedule schedule = scheduleSetUp.saveSchedule(BigDecimal.valueOf(50000.00),
                LocalDateTime.of(2025,1,15,20,0,0),
                LocalDateTime.of(2025,1,1, 10,0,0),
                LocalDateTime.of(2025,1,10,18,0,0),
                50, 100, concert, seatList);

        //when
        Schedule findSchedule = concertService.findByConcertWithScheduleWithSeat(concert.getId(), schedule.getId());
        //then
        assertEquals(schedule.getId(), findSchedule.getId());
        assertEquals(schedule.getSeats().get(0).getId(), findSchedule.getSeats().get(0).getId());
    }

    @Test
    void 예약_가능한_좌석_조회_시_예약_가능한_좌석이_존재하지_않으면_AvailableSeatNotFoundException_발생(){
        //given
        Concert concert = concertSetUp.saveConcert("Awesome Concert", scheduleList);
        //when & then
        Exception exception = assertThrows(AvailableSeatNotFoundException.class,
                ()-> concertService.findByConcertWithScheduleWithSeat(concert.getId(), 999L));

        assertEquals("예약 가능한 좌석 정보를 찾을 수 없습니다.", exception.getMessage());
    }
}
