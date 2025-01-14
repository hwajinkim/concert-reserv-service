package kr.hhplus.be.server.interfaces.api.dto.concert;

import kr.hhplus.be.server.domain.concert.Schedule;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

public record ConcertResponse(
        Long concertId,
        String concertName,
        List<ScheduleResponse> scheduleResponses
) {
}
