package kr.hhplus.be.server.integration_test.application.set_up;

import kr.hhplus.be.server.domain.concert.Concert;
import kr.hhplus.be.server.domain.concert.Schedule;
import kr.hhplus.be.server.domain.concert.Seat;
import kr.hhplus.be.server.infrastructure.schedule.ScheduleJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class ScheduleSetUp {

    @Autowired
    private ScheduleJpaRepository scheduleJpaRepository;

    public Schedule saveSchedule(BigDecimal price, LocalDateTime concertDateTime, LocalDateTime bookingStart,
                                 LocalDateTime bookingEnd, int remainingTicket, int totalTicket, Concert concert,
                                 List<Seat> seatList){
        Schedule schedule = Schedule.builder()
                .price(price)
                .concertDateTime(concertDateTime)
                .bookingStart(bookingStart)
                .bookingEnd(bookingEnd)
                .remainingTicket(remainingTicket)
                .totalTicket(totalTicket)
                .concert(concert)
                .seats(new ArrayList<>())
                .build();

        // 관계 설정
        for(Seat seat : seatList){
            schedule.addSeat(seat);
        }

        return scheduleJpaRepository.save(schedule);
    }

}
