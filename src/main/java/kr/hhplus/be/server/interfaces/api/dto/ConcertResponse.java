package kr.hhplus.be.server.interfaces.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ConcertResponse {
    private Long concertId;
    private String concertName;
    private List<ScheduleResponse> scheduleResponses;
}
