package kr.hhplus.be.server.interfaces.scheduler;

import kr.hhplus.be.server.application.dto.reservation.ReservationParam;
import kr.hhplus.be.server.application.reservation.ReservationFacade;
import kr.hhplus.be.server.domain.reservation.ReservationService;
import kr.hhplus.be.server.interfaces.api.dto.reservation.ReservationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@Component
@RequiredArgsConstructor
public class ReservationScheduler {

    private final ReservationFacade reservationFacade;
    @Scheduled(fixedRate = 1000) // 1초마다 실행
    public void reservationExpiredCheck(){
        //임시 배정(5분) 만료 체크 후 다른 사용자가 이용할 수 있게 상태 변경
        reservationFacade.checkReservationExpiration();
    }
}
