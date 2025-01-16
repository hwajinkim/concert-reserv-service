package kr.hhplus.be.server.application.dto.concert;

import java.time.LocalDateTime;

public record ScheduleResult(
        Long scheduleId,
        LocalDateTime concertDateTime,
        LocalDateTime bookingStart,
        LocalDateTime bookingEnd,
        int remainingTicket
) {
}
