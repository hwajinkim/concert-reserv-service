package kr.hhplus.be.server.application.dto.user;

import kr.hhplus.be.server.interfaces.api.dto.user.UserBalanceRequest;

import java.math.BigDecimal;

public record UserBalanceParam(
        Long userId,
        BigDecimal amount
) {
    public static UserBalanceParam from(UserBalanceRequest userBalanceRequest){
        // 유효성 검증 추가 가능
        if (userBalanceRequest.userId() == null || userBalanceRequest.amount() == null) {
            throw new IllegalArgumentException("User ID와 Amount는 필수입니다.");
        }
        return new UserBalanceParam(
                userBalanceRequest.userId(),
                userBalanceRequest.amount()
        );
    }
}
