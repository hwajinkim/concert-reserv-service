package kr.hhplus.be.server.interfaces.api.dto.user;

import kr.hhplus.be.server.domain.user.User;

import java.math.BigDecimal;


public record UserBalanceResponse(
    Long userId,
    BigDecimal balance) {

    public static UserBalanceResponse from(User user){
        return new UserBalanceResponse(user.getId(), user.getPointBalance());
    }

}
