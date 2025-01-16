package kr.hhplus.be.server.interfaces.api.user;

import io.swagger.v3.oas.annotations.Operation;

import io.swagger.v3.oas.annotations.tags.Tag;

import kr.hhplus.be.server.common.response.ApiResponse;
import kr.hhplus.be.server.common.response.ResponseCode;
import kr.hhplus.be.server.domain.user.UserService;
import kr.hhplus.be.server.interfaces.api.dto.user.UserBalanceRequest;
import kr.hhplus.be.server.interfaces.api.dto.user.UserBalanceResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Tag(name = "잔액 충전/조회 API", description = "잔액을 충전하고 조회하는 api 입니다.")
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // 잔액 조회 API
    @Operation(summary = "잔액 조회")
    @GetMapping("/users/{userId}/balance")
    public ApiResponse<UserBalanceResponse> getBalance(@PathVariable("userId") Long userId){
        return ApiResponse.success(ResponseCode.BALANCE_READ_SUCCESS.getMessage(), UserBalanceResponse.from(userService.findById(userId)));
    }

    // 잔액 충전 API
    @Operation(summary = "잔액 충전")
    @PutMapping("/users/charge")
    public ApiResponse<UserBalanceResponse> chargeBalance(@RequestBody UserBalanceRequest userBalanceRequest){
        return ApiResponse.success(ResponseCode.BALANCE_CHARGE_SUCCESS.getMessage(), UserBalanceResponse.from(userService.charge(userBalanceRequest.userId(), userBalanceRequest.amount())));
    }
}
