package kr.hhplus.be.server.interfaces.api.payment;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.hhplus.be.server.application.dto.payment.PaymentParam;
import kr.hhplus.be.server.application.dto.payment.PaymentResult;
import kr.hhplus.be.server.application.payment.PaymentFacade;
import kr.hhplus.be.server.common.response.ApiResponse;
import kr.hhplus.be.server.common.response.ResponseCode;
import kr.hhplus.be.server.domain.payment.PaymentStatus;
import kr.hhplus.be.server.interfaces.api.dto.payment.PaymentRequest;
import kr.hhplus.be.server.interfaces.api.dto.payment.PaymentResponse;
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

    private final PaymentFacade paymentFacade;
    // 결제 API
    @Operation(summary = "결제 신청")
    @PostMapping("/payments")
    public ApiResponse<PaymentResponse> createPayment(@RequestBody PaymentRequest paymentRequest){

        PaymentParam paymentParam = PaymentParam.from(paymentRequest);

        return ApiResponse.success(ResponseCode.PAYMENT_CREATED_SUCCESS.getMessage(), PaymentResponse.from(paymentFacade.createPayment(paymentParam)));
    }
}
