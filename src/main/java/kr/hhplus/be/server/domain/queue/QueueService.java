package kr.hhplus.be.server.domain.queue;

import kr.hhplus.be.server.common.exception.UserNotFoundException;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class QueueService {

    private final QueueRepository queueRepository;
    @Transactional
    public Queue createQueueToken(Long userId) {
        Queue queue = new Queue();
        Queue createdQueue = queue.create(userId);
        Queue savedQueue = queueRepository.save(createdQueue);
        return savedQueue;
    }
}
