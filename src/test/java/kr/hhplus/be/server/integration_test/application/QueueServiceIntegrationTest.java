package kr.hhplus.be.server.integration_test.application;

import kr.hhplus.be.server.domain.queue.Queue;
import kr.hhplus.be.server.domain.queue.QueueRepository;
import kr.hhplus.be.server.domain.queue.QueueService;
import kr.hhplus.be.server.domain.queue.QueueStatus;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.integration_test.application.set_up.QueueSetUp;
import kr.hhplus.be.server.integration_test.application.set_up.UserSetUp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class QueueServiceIntegrationTest extends BaseIntegrationTest{

    @Autowired
    private QueueService queueService;
    @Autowired
    private QueueSetUp queueSetUp;
    @Autowired
    private UserSetUp userSetUp;
    @Autowired
    private QueueRepository queueRepository;
    private User savedUser;

    @BeforeEach
    void setUp() {
        savedUser = userSetUp.saveUser("김화진", BigDecimal.valueOf(50000.00));
    }

    @Test
    void 대기상태_토큰이_20개일_때_10개만_토큰_활성화(){
        //given

        for (int i = 0; i < 20; i++) {
            Queue savedQueue = queueSetUp.saveQueue(savedUser.getId(), QueueStatus.WAIT, LocalDateTime.now().plusMinutes(10));
        }
        //when
        queueService.activeToken();
        //then
        assertEquals(10, queueRepository.findTopNByActiveStatusOrderByCreatedAt("ACTIVE").size());
    }

    @Test
    void 토큰의_만료_시각이_지났으면_제거(){
        //given
        for (int i = 0; i < 20; i++) {
            Queue savedQueue = queueSetUp.saveQueue(savedUser.getId(), QueueStatus.WAIT, LocalDateTime.now().minusMinutes(10));
        }
        //when
        int deletedCount = queueService.deleteToken();
        //then
        assertEquals(20, deletedCount);
    }

}
