package kr.hhplus.be.server.infrastructure.queue;

import kr.hhplus.be.server.domain.queue.Queue;
import kr.hhplus.be.server.domain.queue.QueueStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QueueJpaRepository extends JpaRepository<Queue,Long> {
    Queue findByUserId(Long userId);

    @Query("SELECT q FROM Queue q WHERE q.queueStatus = :queueStatus ORDER BY q.createdAt ASC")
    List<Queue> findTopNByStatusOrderByCreatedAt(@Param("queueStatus")QueueStatus queueStatus, int limit);
}
