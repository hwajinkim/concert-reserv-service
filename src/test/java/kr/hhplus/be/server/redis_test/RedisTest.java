package kr.hhplus.be.server.redis_test;

import kr.hhplus.be.server.domain.queue.Queue;
import kr.hhplus.be.server.domain.queue.QueueStatus;
import kr.hhplus.be.server.infrastructure.queue.QueueRedisRepositoryImpl;
import kr.hhplus.be.server.infrastructure.queue.QueueRepositoryImpl;
import kr.hhplus.be.server.integration_test.application.BaseIntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;


import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

@SpringBootTest
@ActiveProfiles("redis")
public class RedisTest{

    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private QueueRedisRepositoryImpl queueRedisRepository;

    @Autowired
    private QueueRepositoryImpl queueRepository;

    private static final int THREAD_COUNT = 10;

    @BeforeEach
    void setUp(){
        redisTemplate.getConnectionFactory().getConnection().flushAll();
    }

    @Test
    void redis_대기열에_데이터_추가_조회_속도_테스트() throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(THREAD_COUNT);
        CountDownLatch latch = new CountDownLatch(THREAD_COUNT);
        AtomicLong totalRedisTime = new AtomicLong();


        Callable<Void> task = () -> {
            Long userId = 1L;
            long startTime = System.nanoTime();
            String token = queueRedisRepository.createWaitingQueue(userId);
            String redisValue = queueRedisRepository.getWaitingQueue();  // Redis에서 데이터 조회
            long endTime = System.nanoTime();

            long redisTime = endTime - startTime;

            totalRedisTime.addAndGet(redisTime);

            assertThat(redisValue).isEqualTo(token);

            latch.countDown();
            return null;
        };

        for (int i = 0; i < THREAD_COUNT; i++) {
            executorService.submit(task);
        }

        latch.await();

        // 총 수행 시간 계산 및 출력
        long averageTimeInNanos = totalRedisTime.get() / THREAD_COUNT;
        double averageTimeInSeconds = averageTimeInNanos / 1_000_000_000.0;
        long averageTimeInMillis = TimeUnit.NANOSECONDS.toMillis(averageTimeInNanos);

        System.out.printf("Redis 평균 처리 시간: %.3f 초 (%d ms)%n", averageTimeInSeconds, averageTimeInMillis);

        // 스레드 풀 종료
        executorService.shutdown();
    }

    // 데이터베이스에 데이터 추가 및 조회 속도 테스트
    @Test
    void DB_대기열에_데이터_추가_조회_속도_테스트() throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(THREAD_COUNT);
        CountDownLatch latch = new CountDownLatch(THREAD_COUNT);
        AtomicLong totalDBTime = new AtomicLong();

        Callable<Void> task = () -> {
            try {
                Queue queue = Queue.builder()
                        .userId(ThreadLocalRandom.current().nextLong(1, 1000))  // 각 스레드가 다른 userId 사용
                        .queueStatus(QueueStatus.WAIT)
                        .expiredAt(LocalDateTime.now().plusMinutes(10))
                        .build();

                long startTime = System.nanoTime();
                queueRepository.save(queue);  // DB에 데이터 추가
                Queue dbValue = queueRepository.findByUserId(queue.getUserId());
                long endTime = System.nanoTime();

                long dbTime = endTime - startTime;
                totalDBTime.addAndGet(dbTime);

                assertThat(dbValue.getUserId()).isEqualTo(queue.getUserId());
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                latch.countDown();
            }
            return null;
        };

        for (int i = 0; i < THREAD_COUNT; i++) {
            executorService.submit(task);
        }

        if (!latch.await(30, TimeUnit.SECONDS)) {
            System.err.println("테스트가 시간 초과로 종료되었습니다.");
        }

        long averageTimeInNanos = totalDBTime.get() / THREAD_COUNT;
        double averageTimeInSeconds = averageTimeInNanos / 1_000_000_000.0;
        long averageTimeInMillis = TimeUnit.NANOSECONDS.toMillis(averageTimeInNanos);

        System.out.printf("DB 평균 처리 시간: %.3f 초 (%d ms)%n", averageTimeInSeconds, averageTimeInMillis);

        executorService.shutdown();
        executorService.awaitTermination(30, TimeUnit.SECONDS);
    }

}
