package kr.hhplus.be.server.domain.seat;

import kr.hhplus.be.server.common.exception.AvailableSeatNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SeatService {

    private final SeatRepository seatRepository;
    public List<Seat> getAvailableSeats(Long scheduleId) {
        List<Seat> availableSeats = seatRepository.findAvailableSeatsByScheduleId(scheduleId);
        if(availableSeats == null){
            throw new AvailableSeatNotFoundException("예약 가능한 좌석 정보를 찾을 수 없습니다.");
        }
        return availableSeats;
    }
}
