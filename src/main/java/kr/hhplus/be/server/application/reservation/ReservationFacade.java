package kr.hhplus.be.server.application.reservation;

import kr.hhplus.be.server.application.dto.reservation.ReservationParam;
import kr.hhplus.be.server.application.dto.reservation.ReservationResult;
import kr.hhplus.be.server.domain.concert.Concert;
import kr.hhplus.be.server.domain.concert.ConcertService;
import kr.hhplus.be.server.domain.concert.Schedule;
import kr.hhplus.be.server.domain.concert.ScheduleService;
import kr.hhplus.be.server.domain.dto.SeatScheduleResult;
import kr.hhplus.be.server.domain.reservation.Reservation;
import kr.hhplus.be.server.domain.reservation.ReservationService;
import kr.hhplus.be.server.domain.seat.Seat;
import kr.hhplus.be.server.domain.seat.SeatService;
import kr.hhplus.be.server.domain.seat.SeatStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class ReservationFacade {

    private final SeatService seatService;
    private final ConcertService concertService;
    private final ReservationService reservationService;
    @Transactional
    public ReservationResult createSeatReservation(ReservationParam reservationParam) {
        //1. 좌석(seat) 상태 업데이트 (점유)
        Seat updatedSeat = concertService.updateSeatStatus(reservationParam.seatId(), SeatStatus.OCCUPIED);
        //2. 스케줄(schedule) 잔여 티켓 수 업데이트(-1)
        Schedule updatedSchedule = concertService.updateScheduleRemainingTicket(reservationParam.scheduleId(), -1);
        //3. 예약(reservation) 신청
        Reservation savedReservation = reservationService.creatSeatReservation(updatedSeat, reservationParam.userId());

        return new ReservationResult(savedReservation.getId(), updatedSchedule.getId(),
                savedReservation.getSeatId(), savedReservation.getUserId(), savedReservation.getReservationState(), savedReservation.getCreatedAt());
    }
}
