package kr.hhplus.be.server.unit_test.domain.model;

import kr.hhplus.be.server.common.exception.LackBalanceException;
import kr.hhplus.be.server.domain.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import static org.assertj.core.api.Assertions.*;

public class UserTest {

    private BigDecimal balanceBefore;
    private User user;

    @BeforeEach
    void setUp(){
        balanceBefore = BigDecimal.valueOf(500000.00);
        user = User.builder()
                .pointBalance(balanceBefore)
                .build();
    }

    @Test
    void 포인트_충전_금액이_0원_이하일_때_IllegalArgumentException_발생(){
        //given
        BigDecimal amount = BigDecimal.ZERO;

        //when & then
        assertThatThrownBy(()->user.charge(amount))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("충전 금액은 0보다 커야 합니다.");
    }

    @Test
    void 충전_금액_기존_잔액의_합이_1000000원_초과일_때_IllegalArgumentException_발생(){
        //given
        BigDecimal amount = BigDecimal.valueOf(1000000.00);

        //when & then
        assertThatThrownBy(()->user.charge(amount))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("최대 잔액 100만 포인트를 초과 할 수 없습니다.");
    }

    @Test
    void 충전_금액_기존_잔액의_합이_1000000원_미만이면_포인트_정보_반환(){
        //given
        BigDecimal amount = BigDecimal.valueOf(200000.00);

        //when
        User userCharge = user.charge(amount);

        // then
        assertThat(userCharge.getPointBalance()).isEqualTo(balanceBefore.add(amount));
    }

    @Test
    void 결제_시_기존_잔액보다_사용_금액이_크면_LackBalanceException_발생(){
        //given
        BigDecimal pointBalance = BigDecimal.valueOf(100000.00);
        BigDecimal useAmount = BigDecimal.valueOf(200000.00);

        //when & then
        assertThatThrownBy(()->user.use(pointBalance, useAmount))
                .isInstanceOf(LackBalanceException.class)
                .hasMessage("잔액이 부족합니다.");
    }
}
