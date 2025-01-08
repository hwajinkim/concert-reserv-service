package kr.hhplus.be.server.application.queue;

import kr.hhplus.be.server.application.dto.queue.QueueTokenParam;
import kr.hhplus.be.server.application.dto.queue.QueueTokenResult;
import kr.hhplus.be.server.common.exception.UserNotFoundException;
import kr.hhplus.be.server.domain.queue.Queue;
import kr.hhplus.be.server.domain.queue.QueueService;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.user.UserRepository;
import kr.hhplus.be.server.domain.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QueueFacade {

    private final QueueService queueService;

    private final UserService userService;

    // 유저 대기열 토큰 생성
    public QueueTokenResult createQueueToken(QueueTokenParam queueTokenParam) {
        User user = userService.findById(queueTokenParam.userId());
        Queue queue = queueService.createQueueToken(queueTokenParam.userId());
        return QueueTokenResult.from(queue);
    }
}
