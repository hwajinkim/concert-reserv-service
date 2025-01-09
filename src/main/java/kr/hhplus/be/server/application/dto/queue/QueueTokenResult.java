package kr.hhplus.be.server.application.dto.queue;

import kr.hhplus.be.server.domain.queue.Queue;
import kr.hhplus.be.server.domain.queue.QueueStatus;

import java.time.LocalDateTime;

public record QueueTokenResult(
        Long queueId,
        Long userId,
        QueueStatus queueStatus,
        LocalDateTime createdAt
) {
    public static QueueTokenResult from(Queue queue){
        return new QueueTokenResult(queue.getId(), queue.getUserId(), queue.getQueueStatus(),queue.getCreatedAt());
    }
}
