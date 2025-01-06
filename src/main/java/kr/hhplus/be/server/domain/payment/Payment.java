package kr.hhplus.be.server.domain.payment;

import jakarta.persistence.*;
import kr.hhplus.be.server.domain.common.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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

    private String seatNumber;

    private String concertName;

    private LocalDateTime concertDateTime;

    @Column(nullable = false)
    private BigDecimal paymentAmount;

    @Column(nullable = false)
    private PaymentStatus paymentStatus;

    @Builder
    public Payment(Long paymentId, Long reservationId, String seatNumber, LocalDateTime concertDateTime,
                   BigDecimal paymentAmount, PaymentStatus paymentStatus){
        this.id = paymentId;
        this.reservationId = reservationId;
        this.seatNumber = seatNumber;
        this.concertDateTime = concertDateTime;
        this.paymentAmount = paymentAmount;
        this.paymentStatus = paymentStatus;
    }
}
