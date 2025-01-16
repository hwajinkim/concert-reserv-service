package kr.hhplus.be.server.application.payment;

import kr.hhplus.be.server.application.dto.payment.PaymentParam;
import kr.hhplus.be.server.application.dto.payment.PaymentResult;
import kr.hhplus.be.server.common.exception.MissingExpiryTimeException;
import kr.hhplus.be.server.common.exception.ReservationExpiredException;
import kr.hhplus.be.server.common.exception.ReservationNotFoundException;
import kr.hhplus.be.server.domain.concert.Concert;
import kr.hhplus.be.server.domain.concert.ConcertService;
import kr.hhplus.be.server.domain.concert.Schedule;
import kr.hhplus.be.server.domain.dto.ReservationCheckResult;
import kr.hhplus.be.server.domain.payment.Payment;
import kr.hhplus.be.server.domain.payment.PaymentService;
import kr.hhplus.be.server.domain.queue.Queue;
import kr.hhplus.be.server.domain.queue.QueueService;
import kr.hhplus.be.server.domain.reservation.ReservationService;
import kr.hhplus.be.server.domain.seat.Seat;
import kr.hhplus.be.server.domain.seat.SeatService;
import kr.hhplus.be.server.domain.seat.SeatStatus;
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

        //예약 시간 만료되었는지 체크
        ReservationCheckResult reservationCheckResult = reservationService.checkReservationExpiration(paymentParam.reservationId(), paymentParam.seatId());

        Payment savedPayment = null;

        //1. *예약* 만료 시간 < 현재 시간이면 (예약 만료시)
        if(reservationCheckResult.isExpired()){
            //1-1. *좌석* 상태 'AVAILABLE'으로 변경
            Seat updatedSeat = concertService.updateSeatStatus(paymentParam.seatId(), SeatStatus.AVAILABLE);

            //1-2. *스케줄* 잔여 티켓 업데이트 +1
            Schedule updatedSchedule = concertService.updateScheduleRemainingTicket(updatedSeat.getSchedule().getId(), 1);

            throw new ReservationExpiredException("예약이 만료되었습니다.");
        } else { //2. *예약* 만료 시간 >= 현재 시간이면
            //2-1. *사용자* 잔액 - *예약* 좌석가격
            User user = userService.use(paymentParam.userId(), reservationCheckResult.reservation().getSeatPrice());

            //2-2. *좌석* 상태 'OCCUPIED'으로 변경
            Seat updatedSeat = concertService.updateSeatStatus(reservationCheckResult.reservation().getSeatId(), SeatStatus.OCCUPIED);

            //2-3. *스케줄* 잔여 티켓 업데이트 -1
            Schedule updatedSchedule = concertService.updateScheduleRemainingTicket(updatedSeat.getSchedule().getId(), -1);

            //2-4. *결제* 생성
            savedPayment = paymentService.createPayment(updatedSchedule, updatedSeat, reservationCheckResult.reservation());

            //2-5. *유저 대기열 토큰* 제거
            Queue updatedQueue = queueService.updateQueue(paymentParam.queueId());
        }
        return new PaymentResult(savedPayment.getId(), savedPayment.getReservationId(), paymentParam.seatId(),
                savedPayment.getConcertDateTime(), savedPayment.getPaymentAmount(), savedPayment.getPaymentStatus(), savedPayment.getCreatedAt());
    }
}
