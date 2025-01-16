package kr.hhplus.be.server.application.payment;

import kr.hhplus.be.server.application.dto.payment.PaymentParam;
import kr.hhplus.be.server.application.dto.payment.PaymentResult;
import kr.hhplus.be.server.common.exception.ReservationBadStatusException;
import kr.hhplus.be.server.domain.concert.ConcertService;
import kr.hhplus.be.server.domain.concert.Schedule;
import kr.hhplus.be.server.domain.payment.Payment;
import kr.hhplus.be.server.domain.payment.PaymentService;
import kr.hhplus.be.server.domain.queue.Queue;
import kr.hhplus.be.server.domain.queue.QueueService;
import kr.hhplus.be.server.domain.reservation.Reservation;
import kr.hhplus.be.server.domain.reservation.ReservationService;
import kr.hhplus.be.server.domain.concert.Seat;
import kr.hhplus.be.server.domain.concert.SeatStatus;
import kr.hhplus.be.server.domain.reservation.ReservationState;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentFacade {
    private final ReservationService reservationService;
    private final ConcertService concertService;
    private final UserService userService;
    private final PaymentService paymentService;
    private final QueueService queueService;

    @Transactional
    public PaymentResult createPayment(PaymentParam paymentParam) {

        //예약 정보 가져오기
        Reservation reservation = reservationService.updateReservation(paymentParam.reservationId(), paymentParam.seatId());

        Payment savedPayment = null;

        if(reservation.getReservationState().equals(ReservationState.PAID)){
            //1. *사용자* 잔액 - *예약* 좌석가격
            User user = userService.use(paymentParam.userId(), reservation.getSeatPrice());

            //2. *좌석* 상태 'OCCUPIED'으로 변경
            Seat updatedSeat = concertService.updateSeatStatus(reservation.getSeatId(), SeatStatus.OCCUPIED);

            //3. *스케줄* 잔여 티켓 업데이트 -1
            Schedule updatedSchedule = concertService.updateScheduleRemainingTicket(updatedSeat.getSchedule().getId(), -1);

            //4. *결제* 생성
            savedPayment = paymentService.createPayment(updatedSchedule, updatedSeat, reservation);

            //5. *유저 대기열 토큰* 제거
            Queue updatedQueue = queueService.updateQueue(paymentParam.queueId());
        } else {
            throw new ReservationBadStatusException("유효하지 않은 예약 상태입니다.");
        }

        return new PaymentResult(savedPayment.getId(), savedPayment.getReservationId(), paymentParam.seatId(),
                savedPayment.getConcertDateTime(), savedPayment.getPaymentAmount(), savedPayment.getPaymentStatus(), savedPayment.getCreatedAt());
    }
}
