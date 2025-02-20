package kr.hhplus.be.server.infrastructure.outbox;

import kr.hhplus.be.server.domain.outbox.Outbox;
import kr.hhplus.be.server.domain.outbox.OutboxStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface OutboxJpaRepository extends JpaRepository<Outbox, Long> {
    List<Outbox> findByStatusAndUpdatedAtBefore(OutboxStatus outboxStatus, LocalDateTime localDateTime);

    List<Outbox> findByStatusAndCreatedAtBefore(OutboxStatus outboxStatus, LocalDateTime localDateTime);

    // reservationId와 이벤트 타입으로 단일 Outbox 레코드를 조회
    @Query("SELECT o FROM Outbox o WHERE o.payload LIKE %:reservationId% AND o.eventType = :eventType AND o.status = :status")
    Optional<Outbox> findByReservationIdAndEventTypeAndStatus(@Param("reservationId") Long reservationId,
                                                              @Param("eventType") String eventType,
                                                              @Param("status") OutboxStatus status);

}
