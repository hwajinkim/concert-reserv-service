package kr.hhplus.be.server.integration_test.inter.set_up;

import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.infrastructure.user.UserJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class UserSetUp {

    @Autowired
    private UserJpaRepository userJpaRepository;

    public User saveUser(String userName, BigDecimal pointBalance){
        User user = User.builder()
                .userName(userName)
                .pointBalance(pointBalance)
                .build();
        return userJpaRepository.save(user);
    }
}
