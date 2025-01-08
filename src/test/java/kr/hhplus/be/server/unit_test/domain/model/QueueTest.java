package kr.hhplus.be.server.unit_test.domain.model;

import kr.hhplus.be.server.domain.queue.Queue;
import kr.hhplus.be.server.domain.queue.QueueStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;


public class QueueTest {

    private Queue queue;

    @BeforeEach
    void setUp(){
        queue = Queue.builder()
                .userId(1L)
                .build();
    }

    @Test
    void 사용자ID가_null일_때_IllegalArgumentException_발생(){
        //given
        Long userId = null;

        //when & then
        assertThatThrownBy(()-> Queue.builder()
                .id(1L)
                .userId(userId)
                .queueStatus(QueueStatus.WAIT)
                .expiredAt(LocalDateTime.of(2025,1,7,2,14,00))
                .removedAt(LocalDateTime.of(2025,1,7,2,14,00))
                .build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("사용자 ID 유효하지 않음.");
    }

    @Test
    void 사용자ID가_0일_때_IllegalArgumentException_발생(){
        //given
        Long userId = 0L;
        //when & then
        assertThatThrownBy(()-> Queue.builder()
                .id(1L)
                .userId(userId)
                .queueStatus(QueueStatus.WAIT)
                .expiredAt(LocalDateTime.of(2025,1,7,2,14,00))
                .removedAt(LocalDateTime.of(2025,1,7,2,14,00))
                .build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("사용자 ID 유효하지 않음.");
    }

    @Test
    void 사용자ID가_음수일_때_IllegalArgumentException_발생(){
        //given
        Long userId = -1L;
        //when & then
        assertThatThrownBy(()-> Queue.builder()
                .id(1L)
                .userId(userId)
                .queueStatus(QueueStatus.WAIT)
                .expiredAt(LocalDateTime.of(2025,1,7,2,14,00))
                .removedAt(LocalDateTime.of(2025,1,7,2,14,00))
                .build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("사용자 ID 유효하지 않음.");
    }

    @Test
    void 사용자_대기열_토큰_생성_테스트(){
        //given
        Long userId = 1L;

        //when
        Queue createdQueue = queue.create(userId);

        //then
        assertThat(createdQueue.getUserId()).isEqualTo(userId);
        assertThat(createdQueue.getQueueStatus()).isEqualTo(QueueStatus.WAIT);
    }

}
