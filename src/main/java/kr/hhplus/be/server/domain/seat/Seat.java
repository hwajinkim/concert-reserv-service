package kr.hhplus.be.server.domain.seat;

import jakarta.persistence.*;
import kr.hhplus.be.server.domain.common.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "seat")
public class Seat extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seat_id", unique = true, nullable = false)
    private Long id;

    @Column(nullable = false)
    private Long scheduleId;

    @Column(nullable = false)
    private String seatNumber;

    @Column(nullable = false)
    private SeatStatus seatStatus;

    @Column(nullable = false)
    private BigDecimal seatPrice;

    @Builder
    public Seat(Long seatId, Long scheduleId, String seatNumber, SeatStatus seatStatus, BigDecimal seatPrice){
        this.id = seatId;
        this.scheduleId = scheduleId;
        this.seatNumber = seatNumber;
        this.seatStatus = seatStatus;
        this.seatPrice = seatPrice;
    }

}
