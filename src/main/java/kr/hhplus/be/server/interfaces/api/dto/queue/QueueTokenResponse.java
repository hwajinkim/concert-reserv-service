package kr.hhplus.be.server.interfaces.api.dto.queue;

import kr.hhplus.be.server.application.dto.queue.QueueTokenResult;
import kr.hhplus.be.server.domain.queue.QueueStatus;

import java.time.LocalDateTime;


public record QueueTokenResponse(
        Long queueId,
        Long userId,
        QueueStatus queueStatus,
        LocalDateTime createdAt,

        LocalDateTime expiredAt
){
    public static QueueTokenResponse from(QueueTokenResult queueTokenResult){
        return new QueueTokenResponse(queueTokenResult.queueId(), queueTokenResult.userId(), queueTokenResult.queueStatus(), queueTokenResult.createdAt(), queueTokenResult.expiredAt());
    }
}
