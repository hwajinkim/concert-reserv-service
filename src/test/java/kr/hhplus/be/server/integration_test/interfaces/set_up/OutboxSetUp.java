package kr.hhplus.be.server.integration_test.interfaces.set_up;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.hhplus.be.server.domain.outbox.Outbox;
import kr.hhplus.be.server.domain.outbox.OutboxRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OutboxSetUp {

    @Autowired
    private OutboxRepository outboxRepository;

    @Autowired
    private ObjectMapper objectMapper;

    public Outbox saveOutbox(Long reservationId){
        Outbox outbox = new Outbox(
                "Reservation",
                "ReservationCreated",
                reservationId.toString()
        );
        return outboxRepository.save(outbox);
    }
}
