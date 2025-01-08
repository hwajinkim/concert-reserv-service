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

        Queue mockQueue = Queue.builder()
                    .id(1L)
                    .userId(userId)
                    .queueStatus(QueueStatus.WAIT)
                    .build();

        when(queueRepository.save(any(Queue.class))).thenReturn(mockQueue);

        //when
        Queue savedQueue = queueService.createQueueToken(userId);

        //then
        assertEquals(savedQueue, mockQueue);
        verify(queueRepository, times(1)).save(any(Queue.class));
    }
}
