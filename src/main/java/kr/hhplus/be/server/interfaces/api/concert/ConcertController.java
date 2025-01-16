package kr.hhplus.be.server.interfaces.api.concert;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.hhplus.be.server.application.concert.ConcertFacade;
import kr.hhplus.be.server.application.dto.concert.ConcertResult;
import kr.hhplus.be.server.application.dto.concert.ScheduleSeatResult;
import kr.hhplus.be.server.common.response.ApiResponse;
import kr.hhplus.be.server.common.response.ResponseCode;
import kr.hhplus.be.server.domain.concert.ConcertService;
import kr.hhplus.be.server.interfaces.api.dto.concert.ConcertResponse;
import kr.hhplus.be.server.interfaces.api.dto.concert.ScheduleResponse;
import kr.hhplus.be.server.interfaces.api.dto.concert.ScheduleSeatResponse;
import kr.hhplus.be.server.interfaces.api.dto.seat.SeatResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Tag(name = "예약 가능 날짜/좌석 조회 API", description = "예약 가능한 날짜와 좌석을 조회하는 api 입니다.")
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ConcertController {

    private final ConcertFacade concertFacade;

    private final ConcertService concertService;
    // 예약 가능 날짜 조회 API
    @Operation(summary = "예약 가능 날짜 조회")
    @GetMapping("/concerts/{concertId}/schedules")
    public ApiResponse<ConcertResponse> getAvailableDates(@PathVariable("concertId") Long concertId){

        ConcertResult concertResult = concertFacade.findByConcertWithSchedule(concertId);

        List<ScheduleResponse> scheduleResponses = concertResult.scheduleResults().stream()
                .map(scheduleResult -> new ScheduleResponse(
                        scheduleResult.scheduleId(),
                        scheduleResult.concertDateTime(),
                        scheduleResult.bookingStart(),
                        scheduleResult.bookingEnd(),
                        scheduleResult.remainingTicket()))
                .toList();

        ConcertResponse concertResponse = new ConcertResponse(concertResult.concertId(), concertResult.concertName(), scheduleResponses);
        return ApiResponse.success(ResponseCode.AVAILABLE_RESERV_DATE_READ_SUCCESS.getMessage(), concertResponse);
    }

    // 예약 가능 좌석 조회 API
    @Operation(summary = "예약 가능 좌석 조회")
    @GetMapping("concerts/{concertId}/schedules/{scheduleId}/seats")
    public ApiResponse<ScheduleSeatResponse> getAvailableSeats(@PathVariable("concertId") Long concertId
                                                                , @PathVariable("scheduleId") Long scheduleId){

        ScheduleSeatResult scheduleSeatResult = concertFacade.findByConcertWithScheduleWithSeat(concertId, scheduleId);

        List<SeatResponse> seatResponses = scheduleSeatResult.availableSeats().stream()
                .map(availableSeat -> new SeatResponse(
                        availableSeat.seatId()))
                .toList();

        ScheduleSeatResponse scheduleSeatResponse = new ScheduleSeatResponse(scheduleSeatResult.scheduleId(), seatResponses);
        return ApiResponse.success(ResponseCode.AVAILABLE_RESERV_SEAT_READ_SUCCESS.getMessage(), scheduleSeatResponse);
    }
}
