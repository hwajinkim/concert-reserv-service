package kr.hhplus.be.server.domain.seat;

import kr.hhplus.be.server.common.exception.AvailableSeatNotFoundException;
import kr.hhplus.be.server.common.exception.SeatNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public Seat updateSeatStatus(Long seatId) {
        //1. 좌석 정보 조회
        Seat seat = seatRepository.findById(seatId)
                .orElseThrow(()-> new SeatNotFoundException("좌석을 찾을 수 없습니다."));
        //2. 좌석 정보 업데이트
        Seat updatedSeat = seat.update(seat);
        //3. 좌석 정보 저장
        return seatRepository.save(updatedSeat);
    }


}
