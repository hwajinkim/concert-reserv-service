package kr.hhplus.be.server.unit_test.domain.application;

import kr.hhplus.be.server.application.dto.queue.QueueTokenParam;
import kr.hhplus.be.server.application.dto.queue.QueueTokenResult;
import kr.hhplus.be.server.application.queue.QueueFacade;
import kr.hhplus.be.server.common.exception.UserNotFoundException;
import kr.hhplus.be.server.domain.queue.Queue;
import kr.hhplus.be.server.domain.queue.QueueService;
import kr.hhplus.be.server.domain.queue.QueueStatus;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.user.UserRepository;
import kr.hhplus.be.server.domain.user.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class QueueFacadeTest {

    @InjectMocks
    private QueueFacade queueFacade;

    @Mock
    private QueueService queueService;

    @Mock
    private UserService userService;

    @Test
    void 사용자_대기열_토큰_발급_시_사용자_없으면_UserNotFoundException_발생(){
        //given
        Long userId = 999L;
        QueueTokenParam queueTokenParam = new QueueTokenParam(userId);
        when(userService.findById(userId)).thenThrow(new UserNotFoundException("사용자의 정보가 없습니다."));

        //when & then
        Exception exception = assertThrows(UserNotFoundException.class,
                ()-> queueFacade.createQueueToken(queueTokenParam));

        assertEquals("사용자의 정보가 없습니다.", exception.getMessage());
    }


    @Test
    void 사용자_대기열_토큰_발급_시_Queue_반환(){
        //given
        Long userId = 1L;
        QueueTokenParam queueTokenParam = new QueueTokenParam(userId);
        User mockUser = User.builder()
                .id(userId)
                .userName("김화진")
                .pointBalance(BigDecimal.valueOf(10000.00))
                .build();

        Queue mockQueue = Queue.builder()
                .id(1L)
                .userId(userId)
                .queueStatus(QueueStatus.WAIT)
                .build();

        when(userService.findById(userId)).thenReturn(mockUser);
        when(queueService.createQueueToken(userId)).thenReturn(mockQueue);
        //when
        QueueTokenResult queueTokenResult = queueFacade.createQueueToken(queueTokenParam);
        //then
        assertEquals(queueTokenResult.queueId(), mockQueue.getId());
    }
}
