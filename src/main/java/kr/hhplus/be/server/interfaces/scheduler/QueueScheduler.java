package kr.hhplus.be.server.interfaces.scheduler;

import kr.hhplus.be.server.domain.queue.QueueService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QueueScheduler {
    private final QueueService queueService;

    @Scheduled(fixedRate = 1000) // 1초마다 실행
    public void processQueue() {
        // 대기 토큰 활성화
        queueService.activeToken();
    }

    @Scheduled(fixedRate = 1000) // 1초마다 실행
    public void expiredQueue() {
        // 만료시간 지난 토큰 제거
        queueService.deleteToken();
    }
}
