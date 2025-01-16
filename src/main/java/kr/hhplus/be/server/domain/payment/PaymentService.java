package kr.hhplus.be.server.domain.payment;

import kr.hhplus.be.server.domain.concert.Schedule;
import kr.hhplus.be.server.domain.reservation.Reservation;
import kr.hhplus.be.server.domain.seat.Seat;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;

    @Transactional
    public Payment createPayment(Schedule schedule, Seat seat, Reservation reservation) {
        Payment createdPayment = new Payment().create(schedule, seat, reservation);
        return paymentRepository.save(createdPayment);
    }
}
