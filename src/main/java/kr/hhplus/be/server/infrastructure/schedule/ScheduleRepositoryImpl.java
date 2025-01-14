package kr.hhplus.be.server.infrastructure.schedule;

import kr.hhplus.be.server.domain.concert.Schedule;
import kr.hhplus.be.server.domain.concert.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ScheduleRepositoryImpl implements ScheduleRepository {
    private  final ScheduleJpaRepository scheduleJpaRepository;
    @Override
    public Optional<Schedule> findById(Long scheduleId) {
        return scheduleJpaRepository.findById(scheduleId);
    }

    @Override
    public Schedule save(Schedule schedule) {
        return scheduleJpaRepository.save(schedule);
    }

    @Override
    public Optional<Schedule> findScheduleWithAvailableSeat(Long scheduleId) {
        return scheduleJpaRepository.findScheduleWithAvailableSeat(scheduleId);
    }
}
