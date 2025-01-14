package kr.hhplus.be.server.integration_test.application;

import kr.hhplus.be.server.application.dto.queue.QueueTokenParam;
import kr.hhplus.be.server.application.dto.queue.QueueTokenResult;
import kr.hhplus.be.server.application.queue.QueueFacade;
import kr.hhplus.be.server.common.exception.QueueNotFoundException;
import kr.hhplus.be.server.common.exception.UserNotFoundException;
import kr.hhplus.be.server.domain.queue.Queue;
import kr.hhplus.be.server.domain.queue.QueueStatus;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.integration_test.application.set_up.QueueSetUp;
import kr.hhplus.be.server.integration_test.application.set_up.UserSetUp;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Slf4j
public class QueueFacadeIntegrationTest extends BaseIntegrationTest{

    @Autowired
    private UserSetUp userSetUp;

    @Autowired
    private QueueSetUp queueSetUp;

    @Autowired
    private QueueFacade queueFacade;

    @Test
    void 사용자_대기열_토큰_생성(){
        //given
        User savedUser = userSetUp.saveUser("김화진", BigDecimal.valueOf(50000.00));
        QueueTokenParam queueTokenParam = new QueueTokenParam(savedUser.getId());
        //when
        QueueTokenResult queueTokenResult = queueFacade.createQueueToken(queueTokenParam);
        //then
        assertEquals(savedUser.getId(), queueTokenResult.userId());
    }

    @Test
    void 사용자_대기열_토큰_생성_시_사용자_존재하지_않으면_UserNotFoundException_발생(){
        //given
        QueueTokenParam queueTokenParam = new QueueTokenParam(999L);

        //when & then
        Exception exception = assertThrows(UserNotFoundException.class,
                ()-> queueFacade.createQueueToken(queueTokenParam));

        assertEquals("사용자의 정보가 없습니다.", exception.getMessage());
    }

    @Test
    void 인터셉터에서_토큰_검증했을_때_유효함(){
        //given
        User savedUser = userSetUp.saveUser("김화진", BigDecimal.valueOf(50000.00));
        Queue savedQueue = queueSetUp.saveQueue(savedUser.getId(), QueueStatus.ACTIVE);

        //when
        Boolean result = queueFacade.isQueueValidToken(String.valueOf(savedQueue.getId()));
        //then
        assertEquals(true, result);
    }

    @Test
    void 인터셉터에서_토큰_검증했을_때_토큰_존재하지_않으므로_QueueNotFoundException_발생(){
        //given
        //when & then
        Exception exception = assertThrows(QueueNotFoundException.class,
                ()-> queueFacade.isQueueValidToken(String.valueOf(999L)));

        assertEquals("유저 대기열 토큰을 찾을 수 없습니다.", exception.getMessage());
    }
}
