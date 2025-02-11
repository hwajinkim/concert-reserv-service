package kr.hhplus.be.server.application.queue;

import kr.hhplus.be.server.application.dto.queue.QueueTokenParam;
import kr.hhplus.be.server.application.dto.queue.QueueTokenResult;
import kr.hhplus.be.server.application.dto.queue.RedisQueueTokenResult;
import kr.hhplus.be.server.domain.queue.Queue;
import kr.hhplus.be.server.domain.queue.QueueService;
import kr.hhplus.be.server.domain.queue.QueueStatus;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class QueueFacade {

    private final QueueService queueService;

    private final UserService userService;

    private final RedisTemplate redisTemplate;

    // 유저 대기열 토큰 생성
    @Transactional
    public RedisQueueTokenResult createQueueToken(QueueTokenParam queueTokenParam) {
        User user = userService.findById(queueTokenParam.userId());
        //Queue queue = queueService.createQueueToken(queueTokenParam.userId());
        String tokenId = queueService.createQueueToken(queueTokenParam.userId());
        String userKey = "queue:token:" + tokenId;
        String userId = (String) redisTemplate.opsForHash().get(userKey, "userId");
        // 헤더에 코늩
        return RedisQueueTokenResult.from(tokenId, userId);
    }

    // 인터셉터에서 토큰이 있는지와 활성 상태인지 체크
    public boolean isQueueValidToken(String tokenId) {
        return queueService.findById(tokenId);
    }
}
