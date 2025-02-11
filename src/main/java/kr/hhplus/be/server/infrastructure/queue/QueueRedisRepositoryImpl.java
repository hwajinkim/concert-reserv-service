package kr.hhplus.be.server.infrastructure.queue;

import kr.hhplus.be.server.domain.queue.QueueRedisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class QueueRedisRepositoryImpl implements QueueRedisRepository {

    private static final String WAITING_QUEUE_KEY = "waiting-tokens";
    private static final String ACTIVE_QUEUE_KEY = "active-tokens";
    private static final String ACTIVE_QUEUE_EXPIRY_KEY = "active-tokens-expiry";
    private static final long QUEUE_EXPIRE_SECONDS = 300; // 5분 후 만료
    private final RedisTemplate<String, String> redisTemplate;
    private final ZSetOperations<String, String> zSetOperations;
    private final SetOperations<String, String> setOperations;
    private final HashOperations<String, String, String> hashOperations;

    @Autowired
    public QueueRedisRepositoryImpl(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.zSetOperations = redisTemplate.opsForZSet();
        this.setOperations = redisTemplate.opsForSet();
        this.hashOperations = redisTemplate.opsForHash();
    }
    @Override
    public String createWaitingQueue(Long userId) {
        String tokenId = UUID.randomUUID().toString();
        double score = System.currentTimeMillis(); // 현재 시간을 점수로 사용
        zSetOperations.add(WAITING_QUEUE_KEY, tokenId, score);

        String userKey = "queue:token:" + tokenId;
        redisTemplate.opsForHash().put(userKey, "userId", userId.toString());

        // Redis에서 만료 시간 설정 (5분 후 자동 삭제)
        redisTemplate.expire(WAITING_QUEUE_KEY, QUEUE_EXPIRE_SECONDS, TimeUnit.SECONDS);
        return tokenId;
    }

    @Override
    public String getWaitingQueue(){
        return zSetOperations.range(WAITING_QUEUE_KEY, 0, 0).iterator().next();
    }

    @Override
    public Set<String> getWaitingQueues(int tokenCount) {
        return zSetOperations.range(WAITING_QUEUE_KEY, 0, tokenCount - 1);
    }

    @Override
    public void removeWaitingQueue(String tokenId) {
        zSetOperations.remove(WAITING_QUEUE_KEY, tokenId);
    }

    @Override
    public void createActiveQueue(String tokenId) {
        setOperations.add(ACTIVE_QUEUE_KEY, tokenId);

        // Redis에서 만료 시간 설정 (5분 후 자동 삭제)
        //redisTemplate.expire(ACTIVE_QUEUE_KEY, QUEUE_EXPIRE_SECONDS, TimeUnit.SECONDS);
    }

    @Override
    public Boolean isTokenActive(String tokenId) {
        return setOperations.isMember(ACTIVE_QUEUE_KEY, tokenId);
    }

    @Override
    public void removeActiveQueue(String tokenId) {
        setOperations.remove(ACTIVE_QUEUE_KEY, tokenId);
    }

    @Override
    public void removeActiveQueues(List<String> expiredTokens) {
        setOperations.remove(ACTIVE_QUEUE_KEY, expiredTokens);
    }

    @Override
    public void putHashExpiryTime(String tokenId, String expiryTime) {
        hashOperations.put(ACTIVE_QUEUE_EXPIRY_KEY, tokenId, expiryTime);
    }

    @Override
    public Map<String, String> getHashExpiryTime() {
        return hashOperations.entries(ACTIVE_QUEUE_EXPIRY_KEY);
    }

    @Override
    public void removeHashExpiryTime(String expiredToken) {
        hashOperations.delete(ACTIVE_QUEUE_EXPIRY_KEY, expiredToken);
    }
}
