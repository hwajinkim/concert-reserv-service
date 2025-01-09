package kr.hhplus.be.server.domain.payment;

import jakarta.persistence.*;
import kr.hhplus.be.server.domain.common.BaseEntity;
import kr.hhplus.be.server.domain.concert.Schedule;
import kr.hhplus.be.server.domain.reservation.Reservation;
import kr.hhplus.be.server.domain.seat.Seat;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Slf4j
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "payment")
public class Payment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id", unique = true, nullable = false)
    private Long id;

    @Column(nullable = false)
    private Long reservationId;

    private int seatNumber;

    //private String concertName;

    private LocalDateTime concertDateTime;

    @Column(nullable = false)
    private BigDecimal paymentAmount;

    @Column(nullable = false)
    private PaymentStatus paymentStatus;

    @Builder
    public Payment(Long paymentId, Long reservationId, int seatNumber, LocalDateTime concertDateTime,
                   BigDecimal paymentAmount, PaymentStatus paymentStatus){
        this.id = paymentId;
        this.reservationId = reservationId;
        this.seatNumber = seatNumber;
        this.concertDateTime = concertDateTime;
        this.paymentAmount = paymentAmount;
        this.paymentStatus = paymentStatus;
    }

    public Payment create(Schedule schedule, Seat seat, Reservation reservation) {
        return Payment.builder()
                .reservationId(reservation.getId())
                .seatNumber(seat.getSeatNumber())
                .concertDateTime(schedule.getConcertDateTime())
                .paymentAmount(reservation.getSeatPrice())
                .paymentStatus(PaymentStatus.COMPLETED)
                .build();
    }
}
