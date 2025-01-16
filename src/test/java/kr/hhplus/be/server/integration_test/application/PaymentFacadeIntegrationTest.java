package kr.hhplus.be.server.integration_test.application;

import kr.hhplus.be.server.application.dto.payment.PaymentParam;
import kr.hhplus.be.server.application.dto.payment.PaymentResult;
import kr.hhplus.be.server.application.payment.PaymentFacade;
import kr.hhplus.be.server.domain.concert.Concert;
import kr.hhplus.be.server.domain.concert.Schedule;
import kr.hhplus.be.server.domain.queue.Queue;
import kr.hhplus.be.server.domain.queue.QueueStatus;
import kr.hhplus.be.server.domain.reservation.Reservation;
import kr.hhplus.be.server.domain.reservation.ReservationState;
import kr.hhplus.be.server.domain.concert.Seat;
import kr.hhplus.be.server.domain.concert.SeatStatus;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.integration_test.application.set_up.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class PaymentFacadeIntegrationTest extends BaseIntegrationTest{

    @Autowired
    private PaymentFacade paymentFacade;
    @Autowired
    private UserSetUp userSetUp;
    @Autowired
    private QueueSetUp queueSetUp;
    @Autowired
    private ConcertSetUp concertSetUp;
    @Autowired
    private ScheduleSetUp scheduleSetUp;
    @Autowired
    private ReservationSetUp reservationSetUp;
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
    void 결제_신청(){
        //given
        User user = userSetUp.saveUser("김화진", BigDecimal.valueOf(50000.00));

        Queue queue = queueSetUp.saveQueue(user.getId(), QueueStatus.WAIT, LocalDateTime.now().plusMinutes(10));

        Concert concert = concertSetUp.saveConcert("Awesome Concert", scheduleList);

        Schedule schedule = scheduleSetUp.saveSchedule(
                BigDecimal.valueOf(50000.00),
                LocalDateTime.of(2025,1,15,20,0,0),
                LocalDateTime.of(2025,1,1, 10,0,0),
                LocalDateTime.of(2025,1,10,18,0,0),
                50, 100, concert, seatList);

        Reservation reservation = reservationSetUp.saveReservation(
                user.getId(),
                schedule.getSeats().get(0).getId(),
                ReservationState.PANDING,
                schedule.getSeats().get(0).getSeatPrice(),
                LocalDateTime.now().plusMinutes(5)
        );
        //when
        PaymentParam paymentParam = new PaymentParam(reservation.getId(), schedule.getSeats().get(0).getId(), user.getId(), queue.getId());
        PaymentResult paymentResult = paymentFacade.createPayment(paymentParam);
        //then
        assertNotNull(paymentResult);
    }
}
