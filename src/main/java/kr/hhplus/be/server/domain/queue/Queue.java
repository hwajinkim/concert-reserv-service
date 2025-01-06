package kr.hhplus.be.server.domain.queue;

import jakarta.persistence.*;
import kr.hhplus.be.server.domain.common.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "queue")
public class Queue extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="queue_id", unique = true, nullable = false)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private QueueStatus queueStatus;

    private LocalDateTime expiredAt;

    private LocalDateTime removedAt;

    @Builder
    public Queue(Long queueId, Long userId, QueueStatus queueStatus, LocalDateTime expiredAt, LocalDateTime removedAt){
        this.id = queueId;
        this.userId = userId;
        this.queueStatus = queueStatus;
        this.expiredAt = expiredAt;
        this.removedAt = removedAt;
    }
}
