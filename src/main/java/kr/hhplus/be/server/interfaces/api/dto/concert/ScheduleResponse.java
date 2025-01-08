package kr.hhplus.be.server.interfaces.api.dto.concert;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

public record ScheduleResponse (
        Long scheduleId,
        LocalDateTime concertDateTime,
        LocalDateTime bookingStart,
        LocalDateTime bookingEnd,
        int remainingTicket
){

}
