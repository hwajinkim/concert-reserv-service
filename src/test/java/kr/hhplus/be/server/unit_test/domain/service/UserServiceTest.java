package kr.hhplus.be.server.unit_test.domain.service;

import kr.hhplus.be.server.common.exception.UserNotFoundException;
import kr.hhplus.be.server.domain.user.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private PointHistoryRepository pointHistoryRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void 사용자ID로_조회_시_데이터_없으면_UserNotFoundException_발생(){
        //given
        Long userId = 999L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        //when & then
        Exception exception = assertThrows(UserNotFoundException.class,
                ()-> userService.findById(userId));

        assertEquals("사용자의 정보가 없습니다.", exception.getMessage());
    }

    @Test
    void 사용자ID로_조회_시_데이터_있으면_User_반환(){
        //given
        Long userId = 1L;
        User mockUser = User.builder()
                .id(userId)
                .userName("김화진")
                .pointBalance(BigDecimal.valueOf(50000.00))
                .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        //when
        User user = userService.findById(userId);
        //then
        assertEquals(user.getId(), userId);
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void 포인트_충전_시_사용자_정보가_없으면_UserNotFoundException_발생(){
        //given
        Long userId = 999L;
        BigDecimal amount = BigDecimal.valueOf(10000.00);
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        //when & then
        Exception exception = assertThrows(UserNotFoundException.class,
                ()-> userService.findById(userId));
        assertEquals("사용자의 정보가 없습니다.", exception.getMessage());
    }

    @Test
    void 포인트_충전_시_사용자_정보가_있으면_충전(){
        //given
        Long userId = 1L;
        BigDecimal amount = BigDecimal.valueOf(10000.00);
        User mockUser = User.builder()
                .id(userId)
                .userName("김화진")
                .pointBalance(BigDecimal.valueOf(50000.00))
                .build();

        User updatedUser = User.builder()
                .id(userId)
                .userName("김화진")
                .pointBalance(BigDecimal.valueOf(50000.00).add(amount))
                .build();

        PointHistory pointHistory = PointHistory.builder()
                .id(1L)
                .userId(userId)
                .transMethod(TransMethod.CHARGE)
                .transAmount(amount)
                .balanceBefore(mockUser.getPointBalance())
                .balanceAfter(updatedUser.getPointBalance())
                .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);
        when(pointHistoryRepository.save(any(PointHistory.class))).thenReturn(pointHistory);

        //when
        User savedUser = userService.charge(userId, amount);

        //then
        assertEquals(updatedUser, savedUser);
        verify(userRepository,times(1)).save(any(User.class));
    }
}
