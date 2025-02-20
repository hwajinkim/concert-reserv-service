package kr.hhplus.be.server.domain.outbox;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface OutboxRepository {

    Outbox save(Outbox outbox);

    Optional<Outbox> findById(Long outboxId);

    List<Outbox> findByStatusAndUpdatedAtBefore(OutboxStatus outboxStatus, LocalDateTime localDateTime);

    List<Outbox> findByStatusAndCreatedAtBefore(OutboxStatus outboxStatus, LocalDateTime localDateTime);

    Optional<Outbox> findByReservationIdAndEventTypeAndStatus(Long reservationId, String reservationCreated, OutboxStatus outboxStatus);

    void delete(Outbox outbox);

    List<Outbox> findAll();
}
