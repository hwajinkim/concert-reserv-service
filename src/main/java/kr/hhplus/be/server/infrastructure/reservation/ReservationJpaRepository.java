package kr.hhplus.be.server.infrastructure.reservation;

import kr.hhplus.be.server.domain.reservation.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationJpaRepository extends JpaRepository<Reservation, Long> {


    Optional<Reservation> findByIdAndSeatId(Long id, Long seatId);

    @Query("SELECT r FROM Reservation r WHERE r.expiredAt < :now")
    List<Reservation> findExpiredReservation(@Param("now") LocalDateTime now);
}
