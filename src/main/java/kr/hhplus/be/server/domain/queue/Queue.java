package kr.hhplus.be.server.domain.queue;

import jakarta.persistence.*;
import kr.hhplus.be.server.domain.common.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.security.InvalidParameterException;
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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private QueueStatus queueStatus;

    private LocalDateTime expiredAt;

    private LocalDateTime removedAt;

    @Builder
    public Queue(Long id, Long userId, QueueStatus queueStatus,LocalDateTime expiredAt, LocalDateTime removedAt){
        if(userId == null || userId <= 0){
            throw new IllegalArgumentException("사용자 ID 유효하지 않음.");
        }
        this.id = id;
        this.userId = userId;
        this.queueStatus = queueStatus;
        this.expiredAt = expiredAt;
        this.removedAt = removedAt;
    }

    public Queue create(Long userId) {
        return Queue.builder()
                .userId(userId)
                .queueStatus(QueueStatus.WAIT)
                .expiredAt(LocalDateTime.now().plusMinutes(10))
                .build();
    }

    public Queue update(Queue findQueue) {
        return Queue.builder()
                .id(findQueue.getId())
                .userId(findQueue.getUserId())
                .queueStatus(QueueStatus.EXPIRE)
                .expiredAt(LocalDateTime.now())
                .removedAt(LocalDateTime.now())
                .build();
    }
}
