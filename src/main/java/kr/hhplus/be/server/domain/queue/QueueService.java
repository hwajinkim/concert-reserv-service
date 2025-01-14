package kr.hhplus.be.server.domain.queue;

import kr.hhplus.be.server.common.exception.QueueNotFoundException;
import kr.hhplus.be.server.common.exception.UserNotFoundException;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class QueueService {

    private final QueueRepository queueRepository;

    public Queue createQueueToken(Long userId) {
        Queue queue = new Queue();
        Queue createdQueue = queue.create(userId);
        return queueRepository.save(createdQueue);
    }

    public Queue findById(Long tokenQueueId) {
        return queueRepository.findById(tokenQueueId)
                .orElseThrow(()-> new QueueNotFoundException("유저 대기열 토큰을 찾을 수 없습니다."));
    }

    @Transactional
    public Queue updateQueue(Long queueId) {
        Queue findQueue = queueRepository.findById(queueId)
                .orElseThrow(()-> new QueueNotFoundException("유저 대기열 토큰을 찾을 수 없습니다."));

        Queue updatedQueue = findQueue.update(findQueue);
        return queueRepository.save(updatedQueue);
    }

    @Transactional
    public void activeToken() {
        List<Queue> pendingQueues = queueRepository.findTopNByWaitStatusOrderByCreatedAt("WAIT", 10);

        List<Long> queueIds = pendingQueues.stream()
                .map(Queue::getId)
                .collect(Collectors.toList());

        // 활성 상태로 업데이트
        queueRepository.updateQueueStatus(QueueStatus.ACTIVE, queueIds);
    }

    @Transactional
    public void deleteToken() {
        LocalDateTime now = LocalDateTime.now();
        queueRepository.deleteExpiredTokens(now);
    }
}
