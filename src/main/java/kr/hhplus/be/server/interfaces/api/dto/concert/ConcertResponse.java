package kr.hhplus.be.server.interfaces.api.dto.concert;

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
