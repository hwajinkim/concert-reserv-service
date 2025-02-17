package kr.hhplus.be.server.domain.dataplatform;

import kr.hhplus.be.server.domain.reservation.event.ReservationSuccessEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DataPlatformClient {

    public void send(ReservationSuccessEvent reservationSuccessEvent){
        log.info("사용자에게 예약 확인 이메일 발송 - 예약 ID: {}", reservationSuccessEvent.getReservationId());
    }
}
