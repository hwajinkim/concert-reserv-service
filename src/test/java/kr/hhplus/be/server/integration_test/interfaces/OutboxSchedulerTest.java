package kr.hhplus.be.server.integration_test.interfaces;


import kr.hhplus.be.server.domain.common.producer.KafkaProducer;
import kr.hhplus.be.server.domain.outbox.Outbox;
import kr.hhplus.be.server.domain.outbox.OutboxRepository;
import kr.hhplus.be.server.domain.outbox.OutboxService;
import kr.hhplus.be.server.domain.outbox.OutboxStatus;
import kr.hhplus.be.server.interfaces.scheduler.OutboxScheduler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.test.context.EmbeddedKafka;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;

@SpringBootTest
@EmbeddedKafka(partitions = 1, topics = {"reservation.completed"})
public class OutboxSchedulerTest {
    @Autowired
    private OutboxService outboxService;

    @Autowired
    private OutboxRepository outboxRepository;

    @Autowired
    private OutboxScheduler outboxScheduler;

    @Autowired
    private KafkaProducer kafkaProducer;

    @Test
    public void 아웃박스의_이벤트가_5분_지난_뒤에도_INIT이면_재발행_테스트() {
        // GIVEN: INIT 상태의 Outbox 이벤트를 생성하고, updatedAt을 10분 전으로 설정
        Outbox event = new Outbox("Reservation", "ReservationCreated", "{\"reservationId\":123}");
        event.update(OutboxStatus.INIT);
        event.updateDate(LocalDateTime.now().minusMinutes(10), LocalDateTime.now().minusMinutes(10));
        outboxRepository.save(event);

        // kafkaProducer.send 호출 시 아무런 동작을 하지 않도록 설정
        // doNothing().when(kafkaProducer).send(anyString(), anyString());

        // WHEN: 재처리 스케줄러 실행
        outboxScheduler.retryOutboxEvents();

        // THEN: 이벤트의 상태가 PUBLISHED로 업데이트되었는지 검증
        Optional<Outbox> updatedEventOpt = outboxRepository.findById(event.getId());
        assertTrue(updatedEventOpt.isPresent(), "이벤트가 존재해야 합니다.");
        assertEquals(OutboxStatus.PUBLISHED, updatedEventOpt.get().getStatus(),
                "재처리 후 Outbox 이벤트 상태는 PUBLISHED여야 합니다.");
    }

    @Test
    public void 아웃박스의_이벤트가_30일_이상_지났을_떄_삭제하는지_테스트() {
        // GIVEN: PUBLISHED 상태의 Outbox 이벤트를 생성하고, createdAt을 31일 전으로 설정
        Outbox event = new Outbox("Reservation", "ReservationCreated", "{\"reservationId\":456}");
        event.update(OutboxStatus.PUBLISHED);
        event.updateDate(LocalDateTime.now().minusDays(31), LocalDateTime.now().minusDays(31));
        outboxRepository.save(event);

        // WHEN: 정리 스케줄러 실행
        outboxScheduler.cleanupOldPublishedEvents();

        // THEN: 해당 이벤트가 삭제되었는지 검증
        Optional<Outbox> deletedEvent = outboxRepository.findById(event.getId());
        assertTrue(deletedEvent.isEmpty(), "30일 이상 된 PUBLISHED 이벤트는 삭제되어야 합니다.");
    }

}
