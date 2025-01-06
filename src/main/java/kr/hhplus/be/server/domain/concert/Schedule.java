package kr.hhplus.be.server.domain.concert;

import jakarta.persistence.*;
import kr.hhplus.be.server.domain.common.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "schedule")
public class Schedule extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "schedule_id", unique = true, nullable = false)
    private Long id;

    @Column(nullable = false)
    private Long concertId;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false)
    private LocalDateTime concertDate;

    @Column(nullable = false)
    private LocalDateTime bookingStart;

    @Column(nullable = false)
    private LocalDateTime bookingEnd;

    private int remainingTicket;

    private int totalTicket;

    @Builder
    public Schedule(Long scheduleId, Long concertId, BigDecimal price, LocalDateTime concertDate,
                    LocalDateTime bookingStart, LocalDateTime bookingEnd, int remainingTicket, int totalTicket){
        this.id = scheduleId;
        this.concertId = concertId;
        this.price = price;
        this.concertDate = concertDate;
        this.bookingStart = bookingStart;
        this.bookingEnd = bookingEnd;
        this.remainingTicket = remainingTicket;
        this.totalTicket = totalTicket;
    }
}
