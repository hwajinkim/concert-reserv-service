package kr.hhplus.be.server.application.dto.queue;

import kr.hhplus.be.server.domain.queue.Queue;

public record RedisQueueTokenResult(
    String tokenId,
    String userId
) {
    public static RedisQueueTokenResult from(String tokenId, String userId){
        return new RedisQueueTokenResult(tokenId, userId);
    }
}
