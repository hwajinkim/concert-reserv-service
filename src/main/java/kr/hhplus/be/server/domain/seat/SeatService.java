package kr.hhplus.be.server.domain.seat;

import kr.hhplus.be.server.common.exception.AvailableSeatNotFoundException;
import kr.hhplus.be.server.common.exception.SeatNotFoundException;
import kr.hhplus.be.server.domain.concert.Schedule;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SeatService {

    private final SeatRepository seatRepository;

    @Transactional
    public Seat updateSeatStatus(Long seatId, SeatStatus seatStatus) {
        //1. 좌석 정보 조회
        Seat seat = seatRepository.findByIdWithLock(seatId)
                .orElseThrow(()-> new SeatNotFoundException("좌석을 찾을 수 없습니다."));
        //2. 좌석 정보 업데이트
        Seat updatedSeat = seat.update(seat, seatStatus);
        //3. 좌석 정보 저장
        return seatRepository.save(updatedSeat);
    }


    public Long findScheduleIdBySeatId(Long seatId) {
        return (Long) seatRepository.findScheduleIdBySeatId(seatId)
                .orElseThrow(() -> new SeatNotFoundException("좌석에 연결된 스케줄이 없습니다."));
    }

    public Seat findById(Long seatId) {
        return seatRepository.findById(seatId)
                .orElseThrow(()-> new SeatNotFoundException("좌석을 찾을 수 없습니다."));
    }
}
