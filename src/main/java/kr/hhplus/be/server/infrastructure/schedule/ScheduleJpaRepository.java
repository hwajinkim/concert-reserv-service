package kr.hhplus.be.server.infrastructure.schedule;

import kr.hhplus.be.server.domain.concert.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduleJpaRepository extends JpaRepository<Schedule, Long> {
}
