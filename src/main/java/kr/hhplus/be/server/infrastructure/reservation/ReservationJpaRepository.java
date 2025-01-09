package kr.hhplus.be.server.infrastructure.reservation;

import kr.hhplus.be.server.domain.reservation.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReservationJpaRepository extends JpaRepository<Reservation, Long> {
    Optional<Reservation> findByIdAndSeatId(Long id, Long seatId);
}
