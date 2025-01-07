package kr.hhplus.be.server.application.dto.user;

import kr.hhplus.be.server.domain.user.User;

import java.math.BigDecimal;

public record UserBalanceResult(
        Long userId,
        BigDecimal balance) {
    public static UserBalanceResult from(User user){
        return new UserBalanceResult(user.getId(), user.getPointBalance());
    }
}
