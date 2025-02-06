package kr.hhplus.be.server.interfaces.api.dto.queue;

import kr.hhplus.be.server.application.dto.queue.RedisQueueTokenResult;

public record RedisQueueTokenResponse(
        String tokenId,
        String userId
) {
    public static RedisQueueTokenResponse from(RedisQueueTokenResult redisQueueTokenResult){
        return new RedisQueueTokenResponse(redisQueueTokenResult.tokenId(), redisQueueTokenResult.userId());
    }
}
