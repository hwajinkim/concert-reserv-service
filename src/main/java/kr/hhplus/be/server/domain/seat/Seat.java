package kr.hhplus.be.server.domain.seat;

import jakarta.persistence.*;
import kr.hhplus.be.server.domain.common.BaseEntity;
import kr.hhplus.be.server.domain.concert.Schedule;
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
    private int seatNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SeatStatus seatStatus;

    @Column(nullable = false)
    private BigDecimal seatPrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id")
    private Schedule schedule;

    @Builder
    public Seat(Long seatId, int seatNumber, SeatStatus seatStatus, BigDecimal seatPrice, Schedule schedule){
        this.id = seatId;
        this.seatNumber = seatNumber;
        this.seatStatus = seatStatus;
        this.seatPrice = seatPrice;
        this.schedule = schedule;
    }

    public Seat update(Seat seat) {
        return Seat.builder()
                .seatId(seat.getId())
                .seatNumber(seat.getSeatNumber())
                .seatStatus(SeatStatus.OCCUPIED)
                .seatPrice(seat.getSeatPrice())
                .schedule(seat.getSchedule())
                .build();
    }
}
