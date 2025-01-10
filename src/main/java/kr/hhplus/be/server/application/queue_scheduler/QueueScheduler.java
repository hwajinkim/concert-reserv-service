package kr.hhplus.be.server.application.queue_scheduler;

import kr.hhplus.be.server.domain.queue.Queue;
import kr.hhplus.be.server.domain.queue.QueueRepository;
import kr.hhplus.be.server.domain.queue.QueueStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QueueScheduler {

    private final QueueRepository queueRepository;

    @Scheduled(fixedRate = 5000) // 5초마다 실행
    @Transactional
    public void processQueue() {
        // 대기열에서 대기 상태의 사용자 조회
        List<Queue> pendingQueues = queueRepository.findTopNByStatusOrderByCreatedAt(QueueStatus.WAIT, 10);

        // 상태를 'ACTIVE'로 변경
        for (Queue queue : pendingQueues) {
            queue.setStatus(QueueStatus.ACTIVE);
        }

        queueRepository.saveAll(pendingQueues);

        System.out.println("Activated " + pendingQueues.size() + " queues");
    }
}
