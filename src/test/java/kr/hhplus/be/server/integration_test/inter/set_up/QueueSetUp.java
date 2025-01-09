package kr.hhplus.be.server.integration_test.inter.set_up;

import kr.hhplus.be.server.domain.queue.Queue;
import kr.hhplus.be.server.domain.queue.QueueStatus;
import kr.hhplus.be.server.infrastructure.queue.QueueJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class QueueSetUp {

    @Autowired
    private QueueJpaRepository queueJpaRepository;

    public Queue saveQueue(Long userId, QueueStatus queueStatus) {
        return Queue.builder()
                .userId(userId)
                .queueStatus(queueStatus)
                .build();
    }
}
