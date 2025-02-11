package kr.hhplus.be.server.unit_test.domain.service;

import kr.hhplus.be.server.domain.queue.Queue;
import kr.hhplus.be.server.domain.queue.QueueRepository;
import kr.hhplus.be.server.domain.queue.QueueService;
import kr.hhplus.be.server.domain.queue.QueueStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class QueueServiceTest {

    @Mock
    private QueueRepository queueRepository;

    @InjectMocks
    private QueueService queueService;

    @Test
    void 사용자_대기열_토큰_발급_시_Queue_반환(){
        //given
        Long userId = 1L;

        String mockTokenId = UUID.randomUUID().toString();
        Queue mockQueue = Queue.builder()
                    .id(1L)
                    .userId(userId)
                    .queueStatus(QueueStatus.WAIT)
                    .build();

        when(queueRepository.save(any(Queue.class))).thenReturn(mockQueue);

        //when
        String tokenId = queueService.createQueueToken(userId);

        //then
        assertEquals(tokenId, mockTokenId);
        verify(queueRepository, times(1)).save(any(Queue.class));
    }

    @Test
    void 대기_상태의_대기열_토큰_활성화_상태로_변경(){
        //given
        List<Queue> waitQueues = List.of(
                Queue.builder()
                    .id(1L)
                    .userId(1L)
                    .queueStatus(QueueStatus.WAIT)
                    .build(),
                Queue.builder()
                    .id(2L)
                    .userId(1L)
                    .queueStatus(QueueStatus.WAIT)
                    .build(),
                Queue.builder()
                    .id(3L)
                    .userId(1L)
                    .queueStatus(QueueStatus.WAIT)
                    .build()
        );

        when(queueRepository.findTopNByWaitStatusOrderByCreatedAt("WAIT", 10)).thenReturn(waitQueues);

        //when
        queueService.activeToken();

        //then
        verify(queueRepository, times(1)).updateQueueStatus(QueueStatus.ACTIVE, List.of(1L, 2L, 3L));
    }

    @Test
    void 대기_상태의_대기열_토큰_만료_상태로_변경(){
        //given
        //when
        queueService.deleteToken();
        //then
        verify(queueRepository,times(1)).deleteExpiredTokens(any(LocalDateTime.class));
    }
}
