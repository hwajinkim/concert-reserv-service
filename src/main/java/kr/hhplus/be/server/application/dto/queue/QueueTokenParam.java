package kr.hhplus.be.server.application.dto.queue;

import kr.hhplus.be.server.interfaces.api.dto.queue.QueueTokenRequest;

public record QueueTokenParam(
        Long userId
) {
    public static QueueTokenParam from(QueueTokenRequest queueTokenRequest){
        return new QueueTokenParam(
                queueTokenRequest.userId()
        );
    }
}
