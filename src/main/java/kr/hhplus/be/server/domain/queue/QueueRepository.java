package kr.hhplus.be.server.domain.queue;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface QueueRepository {
    Queue save(Queue queue);

    Queue findByUserId(Long userId);

    Optional<Queue> findById(Long queueId);

    List<Queue> findTopNByWaitStatusOrderByCreatedAt(String queueStatus, int limit);

    List<Queue> findTopNByActiveStatusOrderByCreatedAt(String queueStatus);

    void updateQueueStatus(QueueStatus queueStatus, List<Long> queueIds);

    int deleteExpiredTokens(LocalDateTime now);
}
