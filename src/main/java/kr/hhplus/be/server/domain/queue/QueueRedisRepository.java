package kr.hhplus.be.server.domain.queue;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface QueueRedisRepository {

    // waiting queue
    String createWaitingQueue(Long userId);

    String getWaitingQueue();

    Set<String> getWaitingQueues(int tokenCount);

    void removeWaitingQueue(String tokenId);

    // active queue
    void createActiveQueue(String tokenId);

    Boolean isTokenActive(String tokenId);

    void removeActiveQueue(String tokenId);

    void removeActiveQueues(List<String> expiredTokens);

    //hash
    void putHashExpiryTime(String tokenId, String expiryTime);

    Map<String, String> getHashExpiryTime();

    void removeHashExpiryTime(String expiredToken);
}
