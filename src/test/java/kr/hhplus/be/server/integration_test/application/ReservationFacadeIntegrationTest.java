package kr.hhplus.be.server.integration_test.application;

import kr.hhplus.be.server.application.dto.reservation.ReservationParam;
import kr.hhplus.be.server.application.dto.reservation.ReservationResult;
import kr.hhplus.be.server.application.reservation.ReservationFacade;
import kr.hhplus.be.server.domain.concert.Concert;
import kr.hhplus.be.server.domain.concert.Schedule;
import kr.hhplus.be.server.domain.seat.Seat;
import kr.hhplus.be.server.domain.seat.SeatStatus;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.integration_test.application.set_up.ConcertSetUp;
import kr.hhplus.be.server.integration_test.application.set_up.ScheduleSetUp;
import kr.hhplus.be.server.integration_test.application.set_up.UserSetUp;
import kr.hhplus.be.server.interfaces.api.dto.reservation.ReservationRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ReservationFacadeIntegrationTest extends BaseIntegrationTest{

    @Autowired
    private ReservationFacade reservationFacade;

    @Autowired
    private UserSetUp userSetUp;
    @Autowired
    private ConcertSetUp concertSetUp;
    @Autowired
    private ScheduleSetUp scheduleSetUp;

    private List<Schedule> scheduleList;

    private List<Seat> seatList;

    @BeforeEach
    void setup() {
        scheduleList = List.of(
                Schedule.builder()
                        .price(BigDecimal.valueOf(10000.00))
                        .concertDateTime(LocalDateTime.of(2025,1,15,20,0,0))
                        .bookingStart(LocalDateTime.of(2025,1,1, 10,0,0))
                        .bookingEnd(LocalDateTime.of(2025,1,10,18,0,0))
                        .remainingTicket(50)
                        .build(),
                Schedule.builder()
                        .price(BigDecimal.valueOf(15000.00))
                        .concertDateTime(LocalDateTime.of(2025,1,20,18,0,0))
                        .bookingStart(LocalDateTime.of(2025,1,5, 10,0,0))
                        .bookingEnd(LocalDateTime.of(2025,1,14,18,0,0))
                        .remainingTicket(30)
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
                        .seatStatus(SeatStatus.OCCUPIED)
                        .seatPrice(BigDecimal.valueOf(15000.00))
                        .build()
        );
    }

    @Test
    void 좌석_예약_신청(){
        //given
        User user = userSetUp.saveUser("김화진", BigDecimal.valueOf(50000.00));
        Concert concert = concertSetUp.saveConcert("Awesome Concert", scheduleList);
        Schedule schedule = scheduleSetUp.saveSchedule(
                BigDecimal.valueOf(50000.00),
                LocalDateTime.of(2025,1,15,20,0,0),
                LocalDateTime.of(2025,1,1, 10,0,0),
                LocalDateTime.of(2025,1,10,18,0,0),
                50, 100, concert, seatList);

        ReservationParam reservationParam = new ReservationParam(
                schedule.getId(), schedule.getSeats().get(0).getId(), user.getId()
        );
        //when
        ReservationResult reservationResult = reservationFacade.createSeatReservation(reservationParam);
        //then
        assertNotNull(reservationResult);
    }

}
