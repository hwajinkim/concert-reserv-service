package kr.hhplus.be.server.interfaces.api.user;

import io.swagger.v3.oas.annotations.Operation;

import io.swagger.v3.oas.annotations.tags.Tag;

import kr.hhplus.be.server.common.response.ApiResponse;
import kr.hhplus.be.server.common.response.ErrorResponse;
import kr.hhplus.be.server.common.response.ResponseCode;
import kr.hhplus.be.server.interfaces.api.dto.UserBalanceRequest;
import kr.hhplus.be.server.interfaces.api.dto.UserBalanceResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
@Tag(name = "잔액 충전/조회 API", description = "잔액을 충전하고 조회하는 api 입니다.")
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class UserController {

    // 잔액 조회 API
    @Operation(summary = "잔액 조회")
    @GetMapping("/balance")
    public ApiResponse<UserBalanceResponse> getBalance(){
        UserBalanceResponse userBalanceResponse = new UserBalanceResponse(12345L, 100.00);
        return ApiResponse.success(ResponseCode.BALANCE_READ_SUCCESS.getMessage(), userBalanceResponse);
    }

    // 잔액 충전 API
    @Operation(summary = "잔액 충전")
    @PostMapping("/balance/charge")
    public ApiResponse<UserBalanceResponse> chargeBalance(@RequestBody UserBalanceRequest userBalanceRequest){

        double originalAmount = 100.00;
        double addAmount = userBalanceRequest.getAmount();
        double chargeAmount = originalAmount+addAmount;
        UserBalanceResponse userBalanceResponse = new UserBalanceResponse(12345L, chargeAmount);
        return ApiResponse.success(ResponseCode.BALANCE_CHARGE_SUCCESS.getMessage(), userBalanceResponse);
    }
}
