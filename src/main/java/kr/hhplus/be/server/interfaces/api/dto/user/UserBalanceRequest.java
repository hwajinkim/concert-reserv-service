package kr.hhplus.be.server.interfaces.api.dto.user;

import java.math.BigDecimal;

public record UserBalanceRequest(
    Long userId,
    BigDecimal amount
) {
}
