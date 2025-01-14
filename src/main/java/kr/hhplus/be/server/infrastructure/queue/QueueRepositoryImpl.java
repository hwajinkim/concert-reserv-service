package kr.hhplus.be.server.infrastructure.queue;

import kr.hhplus.be.server.domain.queue.Queue;
import kr.hhplus.be.server.domain.queue.QueueRepository;
import kr.hhplus.be.server.domain.queue.QueueStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class QueueRepositoryImpl implements QueueRepository {

    private final QueueJpaRepository queueJpaRepository;
    @Override
    public Queue save(Queue queue) {
        return queueJpaRepository.save(queue);
    }

    @Override
    public Queue findByUserId(Long userId) {
        return queueJpaRepository.findByUserId(userId);
    }

    @Override
    public Optional<Queue> findById(Long queueId) {
        return queueJpaRepository.findById(queueId);
    }
    @Override
    public List<Queue> findTopNByWaitStatusOrderByCreatedAt(String queueStatus, int limit) {
        return queueJpaRepository.findTopNByWaitStatusOrderByCreatedAt(queueStatus, limit);
    }

    @Override
    public void updateQueueStatus(QueueStatus queueStatus, List<Long> queueIds) {
        queueJpaRepository.updateQueueStatus(queueStatus, queueIds);
    }

    @Override
    public int deleteExpiredTokens(LocalDateTime now) {
        return queueJpaRepository.deleteExpiredTokens(now);
    }
}
