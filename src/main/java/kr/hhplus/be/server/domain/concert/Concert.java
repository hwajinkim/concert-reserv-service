package kr.hhplus.be.server.domain.concert;

import jakarta.persistence.*;
import kr.hhplus.be.server.domain.common.BaseEntity;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "concert")
@ToString
public class Concert extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "concert_id", unique = true, nullable = false)
    private Long id;

    @Column(nullable = false)
    private String concertName;

    @OneToMany(mappedBy = "concert", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Schedule> schedules;


    @Builder
    public Concert(Long concertId, String concertName, List<Schedule> schedules){
        this.id = concertId;
        this.concertName = concertName;
        this.schedules = schedules;
    }

    public void addSchedule(Schedule schedule) {
        if (this.schedules == null) {
            this.schedules = new ArrayList<>();
        }

        Schedule updatedSchedule = Schedule.builder()
                .price(schedule.getPrice())
                .concertDateTime(schedule.getConcertDateTime())
                .bookingStart(schedule.getBookingStart())
                .bookingEnd(schedule.getBookingEnd())
                .remainingTicket(schedule.getRemainingTicket())
                .concert(this) // 연관 관계 설정
                .build();

        this.schedules.add(updatedSchedule);
    }
}
