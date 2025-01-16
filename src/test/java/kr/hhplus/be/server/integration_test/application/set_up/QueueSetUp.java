package kr.hhplus.be.server.integration_test.application.set_up;

import kr.hhplus.be.server.domain.queue.Queue;
import kr.hhplus.be.server.domain.queue.QueueStatus;
import kr.hhplus.be.server.infrastructure.queue.QueueJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class QueueSetUp {

    @Autowired
    private QueueJpaRepository queueJpaRepository;

    public Queue saveQueue(Long userId, QueueStatus queueStatus, LocalDateTime localDateTime) {
         Queue queue = Queue.builder()
                .userId(userId)
                .queueStatus(queueStatus)
                 .expiredAt(localDateTime)
                .build();
        return queueJpaRepository.save(queue);
    }
}
