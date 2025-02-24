package kr.hhplus.be.server.interfaces.event.reservation;

import kr.hhplus.be.server.domain.dataplatform.DataPlatformClient;
import kr.hhplus.be.server.domain.reservation.event.ReservationSuccessEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class ReservationEventListener {

    private final DataPlatformClient dataPlatformClient;
    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void reservationDataSendHandler(ReservationSuccessEvent reservationSuccessEvent) {
        dataPlatformClient.send(reservationSuccessEvent);
    }
}
