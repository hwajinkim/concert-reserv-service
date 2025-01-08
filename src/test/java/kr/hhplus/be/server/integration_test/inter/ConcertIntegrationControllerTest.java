package kr.hhplus.be.server.integration_test.inter;

import jakarta.persistence.EntityManager;
import kr.hhplus.be.server.domain.concert.Concert;
import kr.hhplus.be.server.domain.concert.Schedule;
import kr.hhplus.be.server.domain.seat.Seat;
import kr.hhplus.be.server.domain.seat.SeatStatus;
import kr.hhplus.be.server.integration_test.inter.set_up.ConcertSetUp;
import kr.hhplus.be.server.integration_test.inter.set_up.ScheduleSetUp;
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
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ConcertIntegrationControllerTest extends BaseIntegrationTest{

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ConcertSetUp concertSetUp;

    @Autowired
    private ScheduleSetUp scheduleSetUp;

    @Autowired
    private EntityManager entityManager;
    private List<Schedule> scheduleList;

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
    }

    @Test
    void 예약_가능한_콘서트_스케줄_날짜_조회() throws Exception {
        //given

        Concert concert = concertSetUp.saveConcert("Awesome Concert", scheduleList);

        //when
        ResultActions resultActions = mvc.perform(get("/api/v1/concerts/"+concert.getId()+"/schedules")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print());

        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("success", Matchers.is("true")))
                .andExpect(jsonPath("message", Matchers.is("예약 가능 날짜 조회에 성공했습니다.")))
                .andExpect(jsonPath("data", Matchers.is(notNullValue())));
    }

    @Test
    void 예약_가능한_좌석_조회() throws Exception {
        //given
        Concert concert = concertSetUp.saveConcert("Awesome Concert", scheduleList);

        List<Seat> seats = List.of(
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

        Schedule schedule = scheduleSetUp.saveSchedule(
                BigDecimal.valueOf(50000.00),
                LocalDateTime.of(2025,1,15,20,0,0),
                LocalDateTime.of(2025,1,1, 10,0,0),
                LocalDateTime.of(2025,1,10,18,0,0),
                50, 100, concert, seats);


        //when
        ResultActions resultActions = mvc.perform(get("/api/v1/concerts/"+concert.getId()+"/schedules/"+schedule.getId()+"/seats")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print());

        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("success", Matchers.is("true")))
                .andExpect(jsonPath("message", Matchers.is("예약 가능 좌석 조회에 성공했습니다.")))
                .andExpect(jsonPath("data", Matchers.is(notNullValue())));
    }

}
