package kr.hhplus.be.server.interfaces.api.concert;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.hhplus.be.server.common.response.ApiResponse;
import kr.hhplus.be.server.common.response.ResponseCode;
import kr.hhplus.be.server.domain.concert.Concert;
import kr.hhplus.be.server.domain.concert.ConcertService;
import kr.hhplus.be.server.domain.concert.Schedule;
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

    private final ConcertService concertService;
    // 예약 가능 날짜 조회 API
    @Operation(summary = "예약 가능 날짜 조회")
    @GetMapping("/concerts/{concertId}/schedules")
    public ApiResponse<ConcertResponse> getAvailableDates(@PathVariable("concertId") Long concertId){

        Concert concert = concertService.findByConcertWithSchedule(concertId);

        List<ScheduleResponse> scheduleResponses = concert.getSchedules().stream()
                .map(schedule -> new ScheduleResponse(
                        schedule.getId(),
                        schedule.getConcertDateTime(),
                        schedule.getBookingStart(),
                        schedule.getBookingEnd(),
                        schedule.getRemainingTicket()))
                .toList();

        ConcertResponse concertResponse = new ConcertResponse(concert.getId(), concert.getConcertName(), scheduleResponses);
        return ApiResponse.success(ResponseCode.AVAILABLE_RESERV_DATE_READ_SUCCESS.getMessage(), concertResponse);
    }

    // 예약 가능 좌석 조회 API
    @Operation(summary = "예약 가능 좌석 조회")
    @GetMapping("concerts/{concertId}/schedules/{scheduleId}/seats")
    public ApiResponse<ScheduleSeatResponse> getAvailableSeats(@PathVariable("concertId") Long concertId
                                                                , @PathVariable("scheduleId") Long scheduleId){

        Schedule schedule = concertService.findByConcertWithScheduleWithSeat(concertId, scheduleId);

        List<SeatResponse> seatResponses = schedule.getSeats().stream()
                .map(availableSeat -> new SeatResponse(
                        availableSeat.getId()))
                .toList();

        ScheduleSeatResponse scheduleSeatResponse = new ScheduleSeatResponse(schedule.getId(), seatResponses);
        return ApiResponse.success(ResponseCode.AVAILABLE_RESERV_SEAT_READ_SUCCESS.getMessage(), scheduleSeatResponse);
    }
}
