package kr.hhplus.be.server.infrastructure.queue;

import kr.hhplus.be.server.domain.queue.Queue;
import kr.hhplus.be.server.domain.queue.QueueRepository;
import kr.hhplus.be.server.domain.queue.QueueStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

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
    public List<Queue> findTopNByStatusOrderByCreatedAt(QueueStatus queueStatus, int limit) {
        return queueJpaRepository.findTopNByStatusOrderByCreatedAt(queueStatus, limit);
    }

    @Override
    public void saveAll(List<Queue> pendingQueues) {
        queueJpaRepository.saveAll(pendingQueues);
    }
}
