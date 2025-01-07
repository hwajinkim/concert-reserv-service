package kr.hhplus.be.server.interfaces.api.dto.user;

import kr.hhplus.be.server.application.dto.user.UserBalanceResult;

import java.math.BigDecimal;


public record UserBalanceResponse(
    Long userId,
    BigDecimal balance) {

    public static UserBalanceResponse from(UserBalanceResult userBalanceResult){
        return new UserBalanceResponse(userBalanceResult.userId(), userBalanceResult.balance());
    }
}
