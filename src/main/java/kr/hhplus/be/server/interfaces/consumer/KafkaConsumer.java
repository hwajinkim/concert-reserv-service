package kr.hhplus.be.server.interfaces.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.hhplus.be.server.domain.dataplatform.DataPlatformClient;
import kr.hhplus.be.server.domain.outbox.Outbox;
import kr.hhplus.be.server.domain.outbox.OutboxService;
import kr.hhplus.be.server.domain.outbox.OutboxStatus;
import kr.hhplus.be.server.domain.reservation.event.ReservationSuccessEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaConsumer {

    private final ObjectMapper objectMapper;
    private final DataPlatformClient dataPlatformClient;
    private final OutboxService outboxService;

    @KafkaListener(topics = "reservation.completed", groupId = "my-group")
    public void listen(String payload) throws JsonProcessingException {
        log.info("Received payload: " + payload);
        //outbox 상태가 init 이면 publish로 변경

        // 1. 메시지 파싱
        ReservationSuccessEvent event = parseEvent(payload);

        // 2. Outbox 상태 업데이트 (INIT -> PUBLISHED)
        Optional<Outbox> outboxOpt = outboxService.findInitEvent(event.getReservationId());
        outboxOpt.ifPresent(outbox -> {
            outboxService.updateStatus(outbox.getId(), OutboxStatus.PUBLISHED);
            log.info("Outbox 상태 업데이트 완료: outboxId={}", outbox.getId());
        });

        // 3. 데이터 플랫폼 호출
        dataPlatformClient.send(event);
    }

    public ReservationSuccessEvent parseEvent(String payload) throws JsonProcessingException {
        return objectMapper.readValue(payload, ReservationSuccessEvent.class);
    }
}
