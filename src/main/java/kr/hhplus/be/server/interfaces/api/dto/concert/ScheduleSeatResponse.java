package kr.hhplus.be.server.interfaces.api.dto.concert;

import kr.hhplus.be.server.interfaces.api.dto.seat.SeatResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


public record ScheduleSeatResponse(
        Long scheduleId,
        List<SeatResponse> availableSeats
) {
}
