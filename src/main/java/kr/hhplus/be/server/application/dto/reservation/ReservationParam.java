package kr.hhplus.be.server.application.dto.reservation;

import kr.hhplus.be.server.interfaces.api.dto.reservation.ReservationRequest;

public record ReservationParam(
        Long scheduleId,
        Long seatId,

        Long userId

) {
    public static ReservationParam from(ReservationRequest reservationRequest){
        return new ReservationParam(reservationRequest.scheduleId(), reservationRequest.seatId(), reservationRequest.userId());
    }
}
