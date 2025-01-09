package kr.hhplus.be.server.integration_test.inter;


import com.fasterxml.jackson.core.JsonProcessingException;
import kr.hhplus.be.server.domain.concert.Concert;
import kr.hhplus.be.server.domain.concert.Schedule;
import kr.hhplus.be.server.domain.seat.Seat;
import kr.hhplus.be.server.domain.seat.SeatStatus;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.integration_test.inter.set_up.ConcertSetUp;
import kr.hhplus.be.server.integration_test.inter.set_up.ScheduleSetUp;
import kr.hhplus.be.server.integration_test.inter.set_up.UserSetUp;
import kr.hhplus.be.server.interfaces.api.dto.reservation.ReservationRequest;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ReservationIntegrationControllerTest extends BaseIntegrationTest{

    @Autowired
    private WebApplicationContext context;
    @Autowired
    private UserSetUp userSetUp;
    @Autowired
    private ConcertSetUp concertSetUp;
    @Autowired
    private ScheduleSetUp scheduleSetUp;

    private List<Schedule> scheduleList;

    private List<Seat> seatList;

    @BeforeEach
    public void setup() {
        // MockMvc 초기화
        mvc = MockMvcBuilders.webAppContextSetup(context).build();
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
    void 좌석_예약_신청() throws Exception {
        //given
        // 사용자 저장
        User user = userSetUp.saveUser("김화진", BigDecimal.valueOf(50000.00));

        // 콘서트 & 스케줄 저장
        Concert concert = concertSetUp.saveConcert("Awesome Concert", scheduleList);

        // 스케줄 & 좌석 저장
        Schedule schedule = scheduleSetUp.saveSchedule(
                BigDecimal.valueOf(50000.00),
                LocalDateTime.of(2025,1,15,20,0,0),
                LocalDateTime.of(2025,1,1, 10,0,0),
                LocalDateTime.of(2025,1,10,18,0,0),
                50, 100, concert, seatList);

        ReservationRequest reservationRequest = new ReservationRequest(
                schedule.getId(), schedule.getSeats().get(0).getId(), user.getId()
        );
        //when
        ResultActions resultActions = mvc.perform(post("/api/v1/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reservationRequest))
                        .accept(MediaType.APPLICATION_JSON))
                        .andDo(print());

        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("success", Matchers.is("true")))
                .andExpect(jsonPath("message", Matchers.is("좌석 예약에 성공했습니다.")))
                .andExpect(jsonPath("data", Matchers.is(notNullValue())));
    }
}
