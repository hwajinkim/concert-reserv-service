package kr.hhplus.be.server.infrastructure.seat;

import kr.hhplus.be.server.domain.seat.Seat;
import kr.hhplus.be.server.domain.seat.SeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SeatRepositoryImpl implements SeatRepository {

    private final SeatJpaRepository seatJpaRepository;
    @Override
    public List<Seat> findAvailableSeatsByScheduleId(Long scheduleId) {
        return seatJpaRepository.findAvailableSeatsByScheduleId(scheduleId);
    }
}
