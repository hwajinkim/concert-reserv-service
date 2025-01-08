package kr.hhplus.be.server.infrastructure.seat;

import kr.hhplus.be.server.domain.seat.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SeatJpaRepository extends JpaRepository<Seat, Long> {
    @Query("SELECT s FROM Seat s WHERE s.schedule.id = :scheduleId AND s.seatStatus = 'AVAILABLE'")
    List<Seat> findAvailableSeatsByScheduleId(@Param("scheduleId") Long scheduleId);
}
