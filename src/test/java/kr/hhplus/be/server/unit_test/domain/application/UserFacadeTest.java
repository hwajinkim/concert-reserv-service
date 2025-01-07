package kr.hhplus.be.server.unit_test.domain.application;

import kr.hhplus.be.server.application.dto.user.UserBalanceParam;
import kr.hhplus.be.server.application.dto.user.UserBalanceResult;
import kr.hhplus.be.server.application.user.UserFacade;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.user.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserFacadeTest {

    @InjectMocks
    private UserFacade userFacade;

    @Mock
    private UserService userService;

    @Test
    void 사용자ID로_조회_시_포인트_반환(){
        //given
        Long userId = 1L;
        User mockUser = User.builder()
                .id(userId)
                .userName("김화진")
                .pointBalance(BigDecimal.valueOf(10000.00))
                .build();

        when(userService.findById(userId)).thenReturn(mockUser);
        //when
        UserBalanceResult userBalanceResult = userFacade.getBalance(userId);
        //then
        assertEquals(userBalanceResult.userId(), userId);
    }

    @Test
    void 사용자_포인트_충전_후_충전된_포인트_반환(){
        //given
        Long userId = 1L;
        BigDecimal amount = BigDecimal.valueOf(10000.00);
        UserBalanceParam userBalanceParam = new UserBalanceParam(userId, amount);

        User mockUser = User.builder()
                .id(userId)
                .userName("김화진")
                .pointBalance(BigDecimal.valueOf(50000.00))
                .build();

        User updatedUser = User.builder()
                .id(userId)
                .userName("김화진")
                .pointBalance(mockUser.getPointBalance().add(amount))
                .build();

        when(userService.charge(userId, amount)).thenReturn(updatedUser);

        //when
        UserBalanceResult userBalanceResult = userFacade.chargeBalance(userBalanceParam);

        //then
        assertEquals(0, userBalanceResult.balance().compareTo(updatedUser.getPointBalance())); // BigDecimal 비교
    }
}
