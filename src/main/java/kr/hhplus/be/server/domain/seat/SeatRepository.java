package kr.hhplus.be.server.domain.seat;

import java.util.List;
import java.util.Optional;

public interface SeatRepository {
    List<Seat> findAvailableSeatsByScheduleId(Long scheduleId);

    Optional<Seat> findById(Long seatId);


    Seat save(Seat updatedSeat);
}
