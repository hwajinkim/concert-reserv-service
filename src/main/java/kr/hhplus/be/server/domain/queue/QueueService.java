package kr.hhplus.be.server.domain.queue;

import kr.hhplus.be.server.common.exception.QueueNotFoundException;
import kr.hhplus.be.server.common.exception.UserNotFoundException;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class QueueService {

    private final QueueRedisRepository queueRedisRepository;

    public String createQueueToken(Long userId) {
        return queueRedisRepository.createWaitingQueue(userId);
    }

    public Boolean findById(String tokenId) {
        Boolean isMember =  queueRedisRepository.isTokenActive(tokenId);
        return Boolean.TRUE.equals(isMember);
    }

    public void removeQueue(String tokenId) {
        queueRedisRepository.removeActiveQueue(tokenId);
    }

    public void activeToken() {
        Set<String> tokens = queueRedisRepository.getWaitingQueues(10);

        long expiryTime = System.currentTimeMillis() + (5 * 60 * 1000); // 만료시간 5분 설정

        if (tokens != null && !tokens.isEmpty()) {
            for(String tokenId : tokens){
                queueRedisRepository.createActiveQueue(tokenId);
                queueRedisRepository.putHashExpiryTime(tokenId, String.valueOf(expiryTime));
                queueRedisRepository.removeWaitingQueue(tokenId);
            }

            log.info("활성화된 토큰 수:" + tokens.size());
        }
    }

    public int deleteToken() {
        Map<String, String> tokenExpiryMap = queueRedisRepository.getHashExpiryTime();

        int result = 0;

        if(!tokenExpiryMap.isEmpty()){
            long currentTime = System.currentTimeMillis();
            List<String> expiredTokens = new ArrayList<>();
            for (Map.Entry<String, String> entry : tokenExpiryMap.entrySet()) {
                String tokenId = entry.getKey();
                long expiryTime = Long.parseLong(entry.getValue());

                // 만료 시간이 현재 시간보다 작으면 만료된 토큰 리스트에 추가
                if (expiryTime <= currentTime) {
                    expiredTokens.add(tokenId);
                }
            }

            if(!expiredTokens.isEmpty()){
                for(String expiredToken : expiredTokens){
                    queueRedisRepository.removeActiveQueue(expiredToken);
                    queueRedisRepository.removeHashExpiryTime(expiredToken);
                }
            }
            result = expiredTokens.size();
        }
        return result;
    }
}
