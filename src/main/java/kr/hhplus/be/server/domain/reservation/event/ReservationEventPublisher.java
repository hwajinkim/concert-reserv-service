package kr.hhplus.be.server.domain.reservation.event;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class ReservationEventPublisher {
    private final ApplicationEventPublisher applicationEventPublisher;
    public void success(ReservationSuccessEvent reservationSuccessEvent){
        applicationEventPublisher.publishEvent(reservationSuccessEvent);
    }
}
