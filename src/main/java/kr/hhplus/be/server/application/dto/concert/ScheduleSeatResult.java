package kr.hhplus.be.server.application.dto.concert;

import kr.hhplus.be.server.application.dto.seat.SeatResult;

import java.util.List;

public record ScheduleSeatResult(
        Long scheduleId,
        List<SeatResult> availableSeats
) {
}
