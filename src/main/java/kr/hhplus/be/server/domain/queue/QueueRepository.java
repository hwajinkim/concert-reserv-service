package kr.hhplus.be.server.domain.queue;

import java.util.List;
import java.util.Optional;

public interface QueueRepository {
    Queue save(Queue queue);

    Queue findByUserId(Long userId);

    Optional<Queue> findById(Long queueId);

    List<Queue> findTopNByStatusOrderByCreatedAt(QueueStatus queueStatus, int i);

    void saveAll(List<Queue> pendingQueues);
}
