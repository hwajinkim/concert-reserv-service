package kr.hhplus.be.server.interfaces.api.reservation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.hhplus.be.server.application.dto.reservation.ReservationParam;
import kr.hhplus.be.server.application.dto.reservation.ReservationResult;
import kr.hhplus.be.server.application.reservation.ReservationFacade;
import kr.hhplus.be.server.common.response.ApiResponse;
import kr.hhplus.be.server.common.response.ResponseCode;
import kr.hhplus.be.server.domain.reservation.ReservationState;
import kr.hhplus.be.server.interfaces.api.dto.reservation.ReservationRequest;
import kr.hhplus.be.server.interfaces.api.dto.reservation.ReservationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
@Tag(name = "좌석 예약 API", description = "좌석에 대해 예약 신청하는 api 입니다.")
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ReservationController {
    private final ReservationFacade reservationFacade;

    // 예약 API
    @Operation(summary = "좌석 예약 신청")
    @PostMapping("/reservations")
    public ApiResponse<ReservationResponse> createSeatReservation(@RequestHeader(value = "Queue-Token-User-Id", required = false) Long tokenUserId, @RequestBody ReservationRequest reservationRequest){

        ReservationRequest updatedRequest = ReservationRequest.withUserId(tokenUserId, reservationRequest);
        ReservationParam reservationParam = ReservationParam.from(updatedRequest);

        return ApiResponse.success(ResponseCode.SEAT_RESERV_CREATE_SUCCESS.getMessage(), ReservationResponse.from(reservationFacade.createSeatReservation(reservationParam)));
    }
}
