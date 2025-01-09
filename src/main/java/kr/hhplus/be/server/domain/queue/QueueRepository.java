package kr.hhplus.be.server.domain.queue;

public interface QueueRepository {
    Queue save(Queue queue);

    Queue findByUserId(Long userId);
}
