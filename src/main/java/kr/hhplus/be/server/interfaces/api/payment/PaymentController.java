package kr.hhplus.be.server.interfaces.api.payment;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.hhplus.be.server.common.response.ApiResponse;
import kr.hhplus.be.server.common.response.ResponseCode;
import kr.hhplus.be.server.domain.payment.PaymentStatus;
import kr.hhplus.be.server.interfaces.api.dto.PaymentRequeset;
import kr.hhplus.be.server.interfaces.api.dto.PaymentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@Tag(name = "결제 API", description = "결제 api 입니다.")
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class PaymentController {

    // 결제 API
    @Operation(summary = "결제 신청")
    @PostMapping("/reservations/pay")
    public ApiResponse<PaymentResponse> createPayment(@RequestBody PaymentRequeset paymentRequeset){
        PaymentResponse paymentResponse = new PaymentResponse(12345L, 12345L, 10L,
                "Awesome Concert", LocalDateTime.of(2025, 1, 1, 19,0,0),
                100.00, PaymentStatus.COMPLETED, LocalDateTime.of(2025, 1, 1, 12,0,0));
        return ApiResponse.success(ResponseCode.PAYMENT_CREATED_SUCCESS.getMessage(), paymentResponse);
    }
}
