package kr.hhplus.be.server.domain.concert;

import kr.hhplus.be.server.common.exception.ConcertScheduleNotFoundException;
import kr.hhplus.be.server.domain.dto.concert.ConcertDomainResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ConcertService {

    private final ConcertRepository concertRepository;
    public Concert findByConcertWithSchedule(Long concertId) {
        Concert concert = concertRepository.findByConcertWithSchedule(concertId)
                .orElseThrow(()-> new ConcertScheduleNotFoundException("콘서트 스케줄 정보를 찾을 수 없습니다."));

        return concert;
    }

    @Transactional
    public Concert saveConcertWithSchedules(String concertName, List<Schedule> schedules) {
        // Concert 객체 생성
        Concert concert = Concert.builder()
                .concertId(null) // ID는 자동 생성
                .concertName(concertName)
                .schedules(new ArrayList<>()) // 빈 리스트 초기화
                .build();

        // 편의 메서드를 사용하여 관계 설정
        for (Schedule schedule : schedules) {
            concert.addSchedule(schedule);
        }

        // Concert와 연관된 Schedule 모두 저장
        return concertRepository.save(concert);
    }
}
