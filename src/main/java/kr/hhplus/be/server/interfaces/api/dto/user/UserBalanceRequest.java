package kr.hhplus.be.server.interfaces.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

public record UserBalanceRequest(
    Long userId,
    BigDecimal amount
) {
}
