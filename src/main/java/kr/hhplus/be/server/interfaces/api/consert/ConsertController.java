package kr.hhplus.be.server.interfaces.api.consert;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.hhplus.be.server.common.response.ApiResponse;
import kr.hhplus.be.server.common.response.ResponseCode;
import kr.hhplus.be.server.domain.payment.PaymentStatus;
import kr.hhplus.be.server.domain.queue.QueueStatus;
import kr.hhplus.be.server.domain.reservation.ReservationState;
import kr.hhplus.be.server.interfaces.api.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Tag(name = "예약 가능 날짜/좌석 조회 API", description = "예약 가능한 날짜와 좌석을 조회하는 api 입니다.")
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ConsertController {

    // 예약 가능 날짜 조회 API
    @Operation(summary = "예약 가능 날짜 조회")
    @GetMapping("/concerts/schedules")
    public ApiResponse<ConsertResponse> getAvailableDates(){
        ScheduleResponse scheduleResponse = new ScheduleResponse(
                12345L, LocalDateTime.of(2025, 1, 15, 19,0,0), LocalDateTime.of(2025,1,7,10,0,0),
                LocalDateTime.of(2025,1,10,18,0,0), 50);
        ScheduleResponse scheduleResponse_2 = new ScheduleResponse(
                56789L, LocalDateTime.of(2025, 1, 20,15,0,0), LocalDateTime.of(2025,1,5,10,0,0),
                LocalDateTime.of(2025,1,15,18,0,0), 30);

        List<ScheduleResponse> scheduleResponses = new ArrayList<>();
        scheduleResponses.add(scheduleResponse);
        scheduleResponses.add(scheduleResponse_2);

        ConsertResponse consertResponse = new ConsertResponse(12345L, "Awesome Concert", scheduleResponses);

        return ApiResponse.success(ResponseCode.AVAILABLE_RESERV_DATE_READ_SUCCESS.getMessage(), consertResponse);
    }

    // 예약 가능 좌석 조회 API
    @Operation(summary = "예약 가능 좌석 조회")
    @GetMapping("concerts/schedules/seats")
    public ApiResponse<ScheduleSeatResponse> getAvailableSeats(@RequestParam(name="scheduleId") Long scheduleId){

        SeatResponse seatResponse = new SeatResponse(1L);
        SeatResponse seatResponse_2 = new SeatResponse(2L);
        SeatResponse seatResponse_3 = new SeatResponse(3L);

        List<SeatResponse> availableSeats = new ArrayList<>();
        availableSeats.add(seatResponse);
        availableSeats.add(seatResponse_2);
        availableSeats.add(seatResponse_3);

        ScheduleSeatResponse scheduleSeatResponse = new ScheduleSeatResponse(12345L, availableSeats);

        return ApiResponse.success(ResponseCode.AVAILABLE_RESERV_SEAT_READ_SUCCESS.getMessage(), scheduleSeatResponse);
    }
}
