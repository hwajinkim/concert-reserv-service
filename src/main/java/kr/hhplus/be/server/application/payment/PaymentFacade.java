package kr.hhplus.be.server.application.payment;

import kr.hhplus.be.server.application.dto.payment.PaymentParam;
import kr.hhplus.be.server.application.dto.payment.PaymentResult;
import kr.hhplus.be.server.common.exception.MissingExpiryTimeException;
import kr.hhplus.be.server.common.exception.ReservationExpiredException;
import kr.hhplus.be.server.common.exception.ReservationNotFoundException;
import kr.hhplus.be.server.domain.concert.Concert;
import kr.hhplus.be.server.domain.concert.ConcertService;
import kr.hhplus.be.server.domain.concert.Schedule;
import kr.hhplus.be.server.domain.payment.Payment;
import kr.hhplus.be.server.domain.payment.PaymentService;
import kr.hhplus.be.server.domain.queue.Queue;
import kr.hhplus.be.server.domain.queue.QueueService;
import kr.hhplus.be.server.domain.reservation.Reservation;
import kr.hhplus.be.server.domain.reservation.ReservationService;
import kr.hhplus.be.server.domain.reservation.ReservationState;
import kr.hhplus.be.server.domain.seat.Seat;
import kr.hhplus.be.server.domain.seat.SeatService;
import kr.hhplus.be.server.domain.seat.SeatStatus;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentFacade {
    private final ReservationService reservationService;
    private final SeatService seatService;
    private final ConcertService concertService;
    private final UserService userService;
    private final PaymentService paymentService;
    private final QueueService queueService;

    public PaymentResult createPayment(PaymentParam paymentParam) {

        //1. 예약id와 좌석id로 *예약* 조회
        Reservation findReservation = reservationService.findByReservationIdAndSeatId(paymentParam.reservationId(), paymentParam.seatId());

        Payment savedPayment = null;

        if(findReservation.getExpiredAt() != null){
            //2-1. *예약* 만료 시간 < 현재 시간이면 (예약 만료시)
            if(findReservation.getExpiredAt().isBefore(LocalDateTime.now())){
                //2-1-1. *좌석* 상태 'AVAILABLE'으로 변경
                Seat updatedSeat = concertService.updateSeatStatus(paymentParam.seatId(), SeatStatus.AVAILABLE);

                //2-1-2. *스케줄* 잔여 티켓 업데이트 +1
                Long scheduleId = seatService.findScheduleIdBySeatId(paymentParam.seatId());
                Schedule updatedSchedule = concertService.updateScheduleRemainingTicket(scheduleId, 1);

                //2-1-2. *예약* 상태 'CANCELLED'로 업데이트
                Seat findSeat = seatService.findById(paymentParam.seatId());
                Reservation updatedReservation = reservationService.updateSeatReservation(findSeat, paymentParam.reservationId(), paymentParam.userId(), ReservationState.CANCELLED);

                throw new ReservationExpiredException("예약이 만료되었습니다.");
            } else { //2-2. *예약* 만료 시간 >= 현재 시간이면
                //2-2-1. *사용자* 잔액 - *예약* 좌석가격
                User user = userService.use(paymentParam.userId(), findReservation.getSeatPrice());

                //2-2-2. *좌석* 상태 'OCCUPIED'으로 변경
                Seat updatedSeat = concertService.updateSeatStatus(findReservation.getSeatId(), SeatStatus.OCCUPIED);

                //2-2-3. *스케줄* 잔여 티켓 업데이트 -1
                Long scheduleId = seatService.findScheduleIdBySeatId(paymentParam.seatId());
                Schedule updatedSchedule = concertService.updateScheduleRemainingTicket(scheduleId, -1);

                //2-2-4. *예약* 상태 'PAID'로 변경
                Seat findSeat = seatService.findById(paymentParam.seatId());
                Reservation updatedReservation = reservationService.updateSeatReservation(findSeat, paymentParam.reservationId(), paymentParam.userId(), ReservationState.PAID);

                //2-2-5. *결제* 생성
                Schedule findSchedule = concertService.findById(scheduleId);
                savedPayment = paymentService.createPayment(findSchedule, findSeat, findReservation);

                //2-2-6. *유저 대기열 토큰* 제거
                Queue updatedQueue = queueService.updateQueue(paymentParam.queueId());
            }
        }else{
            throw new MissingExpiryTimeException("예약에 만료 시간이 설정되지 않았습니다.");
        }
        return new PaymentResult(savedPayment.getId(), savedPayment.getReservationId(), paymentParam.seatId(),
                savedPayment.getConcertDateTime(), savedPayment.getPaymentAmount(), savedPayment.getPaymentStatus(), savedPayment.getCreatedAt());
    }
}
