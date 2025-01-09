package kr.hhplus.be.server.integration_test.inter.set_up;

import kr.hhplus.be.server.domain.concert.Concert;
import kr.hhplus.be.server.domain.concert.Schedule;
import kr.hhplus.be.server.infrastructure.concert.ConcertJpaRepository;
import kr.hhplus.be.server.infrastructure.schedule.ScheduleJpaRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class ConcertSetUp {

    @Autowired
    private ConcertJpaRepository concertJpaRepository;

    public Concert saveConcert(String concertName, List<Schedule> scheduleList){
        Concert concert = Concert.builder()
                .concertName(concertName)
                .schedules(new ArrayList<>())
                .build();

        // 관계 설정
        for (Schedule schedule : scheduleList) {
            concert.addSchedule(schedule);
        }

        return concertJpaRepository.save(concert);
    }

}
