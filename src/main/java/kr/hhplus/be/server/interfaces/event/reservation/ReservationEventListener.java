package kr.hhplus.be.server.interfaces.event.reservation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.hhplus.be.server.domain.common.producer.KafkaProducer;
import kr.hhplus.be.server.domain.dataplatform.DataPlatformClient;
import kr.hhplus.be.server.domain.outbox.Outbox;
import kr.hhplus.be.server.domain.outbox.OutboxRepository;
import kr.hhplus.be.server.domain.outbox.OutboxService;
import kr.hhplus.be.server.domain.reservation.event.ReservationSuccessEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class ReservationEventListener {

    private final KafkaProducer kafkaProducer;
    private final OutboxService outboxService;
    private final ObjectMapper objectMapper;

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void saveOutbox(ReservationSuccessEvent event) throws JsonProcessingException {
        outboxService.saveOutbox(objectMapper.writeValueAsString(event));
    }
    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void reservationDataSendHandler(ReservationSuccessEvent event) throws JsonProcessingException {
        kafkaProducer.send("reservation.completed", objectMapper.writeValueAsString(event));
    }
}
