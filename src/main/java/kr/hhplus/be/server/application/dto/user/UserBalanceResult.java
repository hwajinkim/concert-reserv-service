package kr.hhplus.be.server.application.dto.user;

import kr.hhplus.be.server.domain.user.User;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record UserBalanceResult(
        Long userId,
        BigDecimal balance,

        LocalDateTime createdAt
) {
    public static UserBalanceResult from(User user){
        return new UserBalanceResult(user.getId(), user.getPointBalance(), user.getCreatedAt());
    }
}
