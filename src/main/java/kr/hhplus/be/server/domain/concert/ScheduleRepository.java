package kr.hhplus.be.server.domain.concert;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

public interface ScheduleRepository{
    Optional<Schedule> findById(Long scheduleId);

    Schedule save(Schedule schedule);
}
