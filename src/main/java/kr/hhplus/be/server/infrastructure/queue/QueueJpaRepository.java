package kr.hhplus.be.server.infrastructure.queue;

import jakarta.persistence.LockModeType;
import kr.hhplus.be.server.domain.queue.Queue;
import kr.hhplus.be.server.domain.queue.QueueStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface QueueJpaRepository extends JpaRepository<Queue,Long> {
    Queue findByUserId(Long userId);

    @Query(value = "SELECT * FROM queue WHERE queue_status = :queueStatus ORDER BY created_at ASC LIMIT :limit", nativeQuery = true)
    List<Queue> findTopNByWaitStatusOrderByCreatedAt(@Param("queueStatus") String queueStatus, @Param("limit") int limit);

    @Modifying
    @Query("UPDATE Queue q SET q.queueStatus = :newStatus WHERE q.id IN :ids")
    void updateQueueStatus(@Param("newStatus") QueueStatus newStatus, @Param("ids") List<Long> ids);

    @Modifying
    @Query("DELETE FROM Queue t WHERE t.expiredAt < :now")
    int deleteExpiredTokens(@Param("now") LocalDateTime now);
}
