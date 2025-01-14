package kr.hhplus.be.server.infrastructure.concert;

import kr.hhplus.be.server.domain.concert.Concert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ConcertJpaRepository extends JpaRepository<Concert, Long> {

    @Query("SELECT c FROM Concert c JOIN FETCH c.schedules s WHERE c.id = :concertId")
    Optional<Concert> findByConcertWithSchedule(@Param("concertId") Long concertId);
}
