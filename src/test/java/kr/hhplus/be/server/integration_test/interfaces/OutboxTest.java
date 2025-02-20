package kr.hhplus.be.server.integration_test.interfaces;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.hhplus.be.server.application.dto.reservation.ReservationParam;
import kr.hhplus.be.server.application.dto.reservation.ReservationResult;
import kr.hhplus.be.server.application.reservation.ReservationFacade;
import kr.hhplus.be.server.domain.dataplatform.DataPlatformClient;
import kr.hhplus.be.server.domain.outbox.Outbox;
import kr.hhplus.be.server.domain.outbox.OutboxRepository;
import kr.hhplus.be.server.domain.outbox.OutboxStatus;
import kr.hhplus.be.server.domain.reservation.event.ReservationSuccessEvent;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;

import java.util.List;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;
import static org.junit.Assert.assertEquals;


@SpringBootTest
@EmbeddedKafka(partitions = 1, topics = {"reservation.completed"})
public class OutboxTest {
    @Autowired
    private ReservationFacade reservationFacade;

    @Autowired
    private OutboxRepository outboxRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private DataPlatformClient dataPlatformClient;

    @Test
    public void 예약_완료_후_아웃박스_데이터_생성되는지_테스트() throws Exception {
        // Given
        ReservationParam param = new ReservationParam(1L, 1L, 100L);

        // When
        ReservationResult result = reservationFacade.createSeatReservation(param);

        // Then
        List<Outbox> outboxList = outboxRepository.findAll();

        // Outbox payload에 포함된 ReservationSuccessEvent의 reservationId가 일치하는지 검증
        Outbox outbox = outboxList.get(0);
        ReservationSuccessEvent event = objectMapper.readValue(outbox.getPayload(), ReservationSuccessEvent.class);
        assertEquals(result.reservationId(), event.getReservationId());
    }

    @Test
    public void 예약_완료_후_컨슈머에서_아웃박스_상태를_변경하고_데이터플랫폼_호출하는지_테스트() throws Exception {
        // Given
        ReservationParam param = new ReservationParam(1L, 1L, 200L);

        // When
        reservationFacade.createSeatReservation(param);

        // Then
        // DataPlatformClient.send()가 호출되는지 Awaitility로 검증
        await().atMost(30, SECONDS).until(() -> {
            List<Outbox> outboxList = outboxRepository.findAll();
            return !outboxList.isEmpty() &&
                    outboxList.stream().allMatch(outbox -> outbox.getStatus() == OutboxStatus.PUBLISHED);
        });

        // Outbox 레코드가 존재하고 모두 PUBLISHED 상태임을 검증
        List<Outbox> outboxRecords = outboxRepository.findAll();
        outboxRecords.forEach(outbox ->
                assertEquals("모든 Outbox 레코드는 PUBLISHED 상태여야 합니다.", OutboxStatus.PUBLISHED, outbox.getStatus())
        );
    }
}
