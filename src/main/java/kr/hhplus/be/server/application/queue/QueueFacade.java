package kr.hhplus.be.server.application.queue;

import kr.hhplus.be.server.application.dto.queue.QueueTokenParam;
import kr.hhplus.be.server.application.dto.queue.QueueTokenResult;
import kr.hhplus.be.server.domain.queue.Queue;
import kr.hhplus.be.server.domain.queue.QueueService;
import kr.hhplus.be.server.domain.queue.QueueStatus;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class QueueFacade {

    private final QueueService queueService;

    private final UserService userService;

    // 유저 대기열 토큰 생성
    @Transactional
    public QueueTokenResult createQueueToken(QueueTokenParam queueTokenParam) {
        User user = userService.findById(queueTokenParam.userId());
        Queue queue = queueService.createQueueToken(queueTokenParam.userId());
        // 헤더에 코늩
        return QueueTokenResult.from(queue);
    }

    // 인터셉터에서 토큰이 있는지와 활성 상태인지 체크
    public boolean isQueueValidToken(String tokenQueueId) {
        Queue queue = queueService.findById(Long.valueOf(tokenQueueId));
        if (queue == null) return false;
        return queue.getQueueStatus().equals(QueueStatus.ACTIVE);
    }
}
