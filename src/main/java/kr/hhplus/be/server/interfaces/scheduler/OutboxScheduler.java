package kr.hhplus.be.server.interfaces.scheduler;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.hhplus.be.server.domain.common.producer.KafkaProducer;
import kr.hhplus.be.server.domain.outbox.Outbox;
import kr.hhplus.be.server.domain.outbox.OutboxService;
import kr.hhplus.be.server.domain.outbox.OutboxStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;


@Slf4j
@Component
@RequiredArgsConstructor
public class OutboxScheduler {
    private final OutboxService outboxService;
    private final KafkaProducer kafkaProducer;
    private final ObjectMapper objectMapper;

    // 재처리 스케줄러: 5분마다 실행
    @Scheduled(fixedDelay = 300000) // 5분
    public void retryOutboxEvents() {
        List<Outbox> pendingEvents = outboxService.findInitEventsOlderThan5Min();
        if (!pendingEvents.isEmpty()) {
            pendingEvents.forEach(event -> {
                try {
                    // Kafka 메시지 재발행
                    kafkaProducer.send("reservation.completed", event.getPayload());
                    // 재발행 성공 시 상태를 PUBLISHED로 업데이트
                    outboxService.updateStatus(event.getId(), OutboxStatus.PUBLISHED);
                    // 재처리 성공 로그
                    log.info("Outbox 이벤트 재처리 성공: 이벤트ID={}", event.getId());
                } catch (Exception e) {
                    // 재처리 실패 시 로그
                    log.error("Outbox 이벤트 재처리 실패: 이벤트ID={}, error={}", event.getId(), e.getMessage());
                }
            });
        }
    }

    // 정리 스케줄러: 5분마다 실행
    @Scheduled(fixedDelay = 300000)
    public void cleanupOldPublishedEvents() {
        List<Outbox> oldEvents = outboxService.findPublishedEventsOlderThan30Days();
        if (!oldEvents.isEmpty()) {
            oldEvents.forEach(event -> {
                try {
                    outboxService.deleteOutbox(event);
                    log.info("오래된 Outbox 이벤트 삭제 완료: 이벤트ID={}", event.getId());
                } catch (Exception e) {
                    log.error("오래된 Outbox 이벤트 삭제 실패: 이벤트ID={}, error={}", event.getId(), e.getMessage());
                }
            });
        }
    }

}
