package kr.hhplus.be.server.integration_test.interfaces;

import kr.hhplus.be.server.application.dto.reservation.ReservationParam;
import kr.hhplus.be.server.application.reservation.ReservationFacade;
import kr.hhplus.be.server.domain.common.producer.KafkaProducer;
import kr.hhplus.be.server.domain.concert.Concert;
import kr.hhplus.be.server.domain.concert.Schedule;
import kr.hhplus.be.server.domain.concert.Seat;
import kr.hhplus.be.server.domain.concert.SeatStatus;
import kr.hhplus.be.server.domain.outbox.Outbox;
import kr.hhplus.be.server.domain.reservation.Reservation;
import kr.hhplus.be.server.domain.reservation.ReservationRepository;
import kr.hhplus.be.server.domain.reservation.ReservationState;
import kr.hhplus.be.server.domain.reservation.event.ReservationSuccessEvent;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.integration_test.application.set_up.ConcertSetUp;
import kr.hhplus.be.server.integration_test.application.set_up.ReservationSetUp;
import kr.hhplus.be.server.integration_test.application.set_up.ScheduleSetUp;
import kr.hhplus.be.server.integration_test.application.set_up.UserSetUp;
import kr.hhplus.be.server.integration_test.interfaces.set_up.OutboxSetUp;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@ExtendWith(SpringExtension.class)
@SpringBootTest
@EmbeddedKafka(partitions = 1, topics = {"reservation.completed"})
public class ReservationConsumerTest {

    @Autowired
    private ReservationFacade reservationFacade;
    @Autowired
    private UserSetUp userSetUp;
    @Autowired
    private ConcertSetUp concertSetUp;
    @Autowired
    private ScheduleSetUp scheduleSetUp;
    @Autowired
    private ReservationSetUp reservationSetUp;
    @Autowired
    private OutboxSetUp outboxSetUp;
    private List<Schedule> scheduleList;
    private List<Seat> seatList;

    @Autowired
    private KafkaProducer kafkaProducer;

    private static final BlockingQueue<String> messages = new LinkedBlockingQueue<>();

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

    @KafkaListener(topics = "reservation.completed", groupId = "my-group")
    public void listen(String payload) {
        log.info("Received payload: {}", payload);
        messages.add(payload);
    }
    @Test
    void reservationConsumerTest() throws InterruptedException {
        //given
        User user = userSetUp.saveUser("김화진", BigDecimal.valueOf(50000.00));
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

        Outbox outBox = outboxSetUp.saveOutbox(reservation.getId());

        kafkaProducer.send("reservation.completed", outBox.getPayload());

        //Consumer가 메시지 정상적으로 수신했는지 확인
        String receivedMessage = messages.poll(30, TimeUnit.SECONDS);
        assertThat(receivedMessage).isEqualTo(outBox.getPayload());
    }
}
