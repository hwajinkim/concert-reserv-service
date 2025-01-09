package kr.hhplus.be.server.application.concert;

import kr.hhplus.be.server.application.dto.concert.ConcertResult;
import kr.hhplus.be.server.application.dto.concert.ScheduleResult;
import kr.hhplus.be.server.application.dto.concert.ScheduleSeatResult;
import kr.hhplus.be.server.application.dto.seat.SeatResult;
import kr.hhplus.be.server.domain.concert.Concert;
import kr.hhplus.be.server.domain.concert.ConcertService;
import kr.hhplus.be.server.domain.seat.Seat;
import kr.hhplus.be.server.domain.seat.SeatService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ConcertFacade {

    private final ConcertService concertService;

    private final SeatService seatService;
    public ConcertResult findByConcertWithSchedule(Long concertId) {
        Concert concert = concertService.findByConcertWithSchedule(concertId);

        List<ScheduleResult> scheduleResults = concert.getSchedules().stream()
                .map(schedule -> new ScheduleResult(
                        schedule.getId(),
                        schedule.getConcertDateTime(),
                        schedule.getBookingStart(),
                        schedule.getBookingEnd(),
                        schedule.getRemainingTicket()))
                .toList();

        return new ConcertResult(concert.getId(), concert.getConcertName(), scheduleResults);
    }

    public ScheduleSeatResult findByConcertWithScheduleWithSeat(Long concertId, Long scheduleId) {
        Concert concert = concertService.findByConcertWithSchedule(concertId);

        List<Seat> availableSeats = seatService.getAvailableSeats(scheduleId);

        List<SeatResult> seatResults = availableSeats.stream()
                .map(seat -> new SeatResult(seat.getId()))
                .toList();

        return new ScheduleSeatResult(scheduleId, seatResults);
    }
}
