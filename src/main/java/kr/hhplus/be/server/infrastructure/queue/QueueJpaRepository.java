package kr.hhplus.be.server.infrastructure.queue;

import kr.hhplus.be.server.domain.queue.Queue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QueueJpaRepository extends JpaRepository<Queue,Long> {
}
