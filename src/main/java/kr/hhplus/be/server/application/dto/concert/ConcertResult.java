package kr.hhplus.be.server.application.dto.concert;

import java.util.List;

public record ConcertResult(
        Long concertId,
        String concertName,
        List<ScheduleResult> scheduleResults
) {
}
