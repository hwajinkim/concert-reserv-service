package kr.hhplus.be.server.integration_test.application;

import kr.hhplus.be.server.common.exception.UserNotFoundException;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.user.UserService;
import kr.hhplus.be.server.integration_test.application.set_up.UserSetUp;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class UserServiceIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private UserSetUp userSetUp;

    @Autowired
    private UserService userService;

    @Test
    void 사용자_잔액_조회_시_포인트_반환() throws Exception {
        //given
        User savedUser = userSetUp.saveUser("김화진", BigDecimal.valueOf(50000.00));

        //when
        User findUser = userService.findById(savedUser.getId());

        //then
        assertEquals(savedUser.getId(), findUser.getId());
        assertEquals(savedUser.getPointBalance().setScale(2, RoundingMode.DOWN), findUser.getPointBalance().setScale(2, RoundingMode.DOWN));
    }

    @Test
    void 사용자_잔액_조회_시_사용자_존재하지_않으면_UserNotFoundException_발생(){
        //given
        //when & then
        Exception exception = assertThrows(UserNotFoundException.class,
                ()-> userService.findById(999L));

        assertEquals("사용자의 정보가 없습니다.", exception.getMessage());
    }

    @Test
    void 포인트_충전_후_사용자_정보_반환(){
        //given
        User savedUser = userSetUp.saveUser("김화진", BigDecimal.valueOf(50000.00));
        BigDecimal amount = BigDecimal.valueOf(10000.00);

        //when
        User user = userService.charge(savedUser.getId(), amount);
        //then
        assertEquals(BigDecimal.valueOf(60000.00).setScale(2, RoundingMode.DOWN), user.getPointBalance().setScale(2, RoundingMode.DOWN));
    }

    @Test
    void 포인트_충전_시_사용자_존재하지_않으면_UserNotFoundException_발생(){
        //when & then
        Exception exception = assertThrows(UserNotFoundException.class,
                ()-> userService.findById(999L));

        assertEquals("사용자의 정보가 없습니다.", exception.getMessage());
    }
}