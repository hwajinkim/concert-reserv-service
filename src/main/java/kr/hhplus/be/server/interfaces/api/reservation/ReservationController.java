package kr.hhplus.be.server.interfaces.api.reservation;

import kr.hhplus.be.server.common.response.ApiResponse;
import kr.hhplus.be.server.common.response.ResponseCode;
import kr.hhplus.be.server.domain.payment.PaymentStatus;
import kr.hhplus.be.server.domain.reservation.ReservationState;
import kr.hhplus.be.server.interfaces.api.dto.PaymentRequeset;
import kr.hhplus.be.server.interfaces.api.dto.PaymentResponse;
import kr.hhplus.be.server.interfaces.api.dto.ReservationRequest;
import kr.hhplus.be.server.interfaces.api.dto.ReservationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ReservationController {

    // 예약 API
    @PostMapping("/concerts/seats/reserve")
    public ApiResponse<ReservationResponse> createSeatReservation(@RequestBody ReservationRequest reservationRequest){

        ReservationResponse reservationResponse = new ReservationResponse(12345L, 12345L, 12345L,
                67890L, ReservationState.PANDING,
                LocalDateTime.of(2025,1,1,12,0,0, 0));
        return ApiResponse.success(ResponseCode.SEAT_RESERV_CREATE_SUCCESS.getMessage(), reservationResponse);
    }

    // 결제 API
    @PostMapping("/reservations/pay")
    public ApiResponse<PaymentResponse> createPayment(@RequestBody PaymentRequeset paymentRequeset){
        PaymentResponse paymentResponse = new PaymentResponse(12345L, 12345L, 10L,
                "Awesome Concert", LocalDateTime.of(2025, 1, 1, 19,0,0),
                100.00, PaymentStatus.COMPLETED, LocalDateTime.of(2025, 1, 1, 12,0,0));
        return ApiResponse.success(ResponseCode.PAYMENT_CREATED_SUCCESS.getMessage(), paymentResponse);
    }
}
