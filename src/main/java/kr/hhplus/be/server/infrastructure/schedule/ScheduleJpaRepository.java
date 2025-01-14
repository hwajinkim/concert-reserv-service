package kr.hhplus.be.server.infrastructure.schedule;

import kr.hhplus.be.server.domain.concert.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ScheduleJpaRepository extends JpaRepository<Schedule, Long> {

    @Query("SELECT c FROM Schedule c JOIN FETCH c.seats s WHERE c.id = :scheduleId AND s.seatStatus = 'AVAILABLE'")
    Optional<Schedule> findScheduleWithAvailableSeat(@Param("scheduleId") Long scheduleId);
}
