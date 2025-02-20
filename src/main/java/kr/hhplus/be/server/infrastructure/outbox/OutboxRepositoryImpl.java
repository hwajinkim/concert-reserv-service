package kr.hhplus.be.server.infrastructure.outbox;

import kr.hhplus.be.server.domain.outbox.Outbox;
import kr.hhplus.be.server.domain.outbox.OutboxRepository;
import kr.hhplus.be.server.domain.outbox.OutboxStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class OutboxRepositoryImpl implements OutboxRepository {

    private final OutboxJpaRepository outboxJpaRepository;

    @Override
    public Outbox save(Outbox outbox) {
        return outboxJpaRepository.save(outbox);
    }

    @Override
    public Optional<Outbox> findById(Long outboxId) {
        return outboxJpaRepository.findById(outboxId);
    }

    @Override
    public List<Outbox> findByStatusAndUpdatedAtBefore(OutboxStatus outboxStatus, LocalDateTime localDateTime) {
        return outboxJpaRepository.findByStatusAndUpdatedAtBefore(outboxStatus, localDateTime);
    }

    @Override
    public List<Outbox> findByStatusAndCreatedAtBefore(OutboxStatus outboxStatus, LocalDateTime localDateTime) {
        return outboxJpaRepository.findByStatusAndCreatedAtBefore(outboxStatus, localDateTime);
    }

    @Override
    public Optional<Outbox> findByReservationIdAndEventTypeAndStatus(Long reservationId, String reservationCreated, OutboxStatus outboxStatus) {
        return outboxJpaRepository.findByReservationIdAndEventTypeAndStatus(reservationId, reservationCreated, outboxStatus);
    }

    @Override
    public void delete(Outbox outbox) {
        outboxJpaRepository.delete(outbox);
    }

    @Override
    public List<Outbox> findAll() {
        return outboxJpaRepository.findAll();
    }

}
