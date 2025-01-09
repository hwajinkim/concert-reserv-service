package kr.hhplus.be.server.application.user;

import kr.hhplus.be.server.application.dto.user.UserBalanceParam;
import kr.hhplus.be.server.application.dto.user.UserBalanceResult;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserFacade {

    private final UserService userService;

    // 사용자 잔액 조회
    public UserBalanceResult getBalance(Long userId) {
        User user = userService.findById(userId);
        return UserBalanceResult.from(user);
    }

    // 사용자 잔액 충전
    public UserBalanceResult chargeBalance(UserBalanceParam userBalanceParam) {

        // 1. 잔액 충전
        User user = userService.charge(userBalanceParam.userId(), userBalanceParam.amount());
        return UserBalanceResult.from(user);
    }
}
