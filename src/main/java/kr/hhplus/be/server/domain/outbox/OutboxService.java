package kr.hhplus.be.server.domain.outbox;

import kr.hhplus.be.server.domain.reservation.event.ReservationSuccessEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OutboxService {

    private final OutboxRepository outboxRepository;

    @Transactional
    public void saveOutbox(String payload) {
        outboxRepository.save(new Outbox("Reservation", "ReservationCreated", payload));
    }

    @Transactional
    public void updateStatus(Long outboxId, OutboxStatus newStatus) {
        Outbox outbox = outboxRepository.findById(outboxId)
                .orElseThrow(() -> new RuntimeException("Outbox 레코드가 존재하지 않습니다."));
        outbox.update(newStatus);
        outboxRepository.save(outbox);
    }

    // 재처리: INIT 상태, updateAt이 5분 지난 아웃박스 조회
    public List<Outbox> findInitEventsOlderThan5Min() {
        return outboxRepository.findByStatusAndUpdatedAtBefore(OutboxStatus.INIT, LocalDateTime.now().minusMinutes(5));
    }

    // 정리: PUBLISHED 상태, createdAt이 30일 지난 경과한 아웃박스 조회
    public List<Outbox> findPublishedEventsOlderThan30Days() {
        return outboxRepository.findByStatusAndCreatedAtBefore(OutboxStatus.PUBLISHED, LocalDateTime.now().minusDays(30));
    }

    public Optional<Outbox> findInitEvent(Long reservationId) {
        return outboxRepository.findByReservationIdAndEventTypeAndStatus(reservationId, "ReservationCreated", OutboxStatus.INIT);
    }

    @Transactional
    public void deleteOutbox(Outbox outbox) {
        outboxRepository.delete(outbox);
    }
}
