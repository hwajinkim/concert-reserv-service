package kr.hhplus.be.server.domain.reservation;

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
@Table(name = "reservation")
public class Reservation extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reservation_id", unique = true, nullable = false)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Long seatId;

    @Column(nullable = false)
    private ReservationState reservationState;

    @Column(nullable = false)
    private BigDecimal seatPrice;

    private LocalDateTime expiredAt;

    @Builder
    public Reservation(Long reservationId, Long userId, Long seatId, ReservationState reservationState,
                       BigDecimal seatPrice, LocalDateTime expiredAt){
        this.id = reservationId;
        this.userId = userId;
        this.seatId = seatId;
        this.reservationState = reservationState;
        this.seatPrice = seatPrice;
        this.expiredAt = expiredAt;
    }
}
