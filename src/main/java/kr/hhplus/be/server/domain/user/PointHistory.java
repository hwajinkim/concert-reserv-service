package kr.hhplus.be.server.domain.user;

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
@Table(name = "point_history")
public class PointHistory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "point_history_id", unique = true, nullable = false)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    private Long paymentId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransMethod transMethod;

    private BigDecimal transAmount;

    private BigDecimal balanceBefore;

    private BigDecimal balanceAfter;

    @Builder
    public PointHistory(Long id, Long userId, Long paymentId
            , TransMethod transMethod, BigDecimal transAmount
            , BigDecimal balanceBefore, BigDecimal balanceAfter){
        this.id = id;
        this.userId = userId;
        this.paymentId = paymentId;
        this.transMethod = transMethod;
        this.transAmount = transAmount;
        this.balanceBefore = balanceBefore;
        this.balanceAfter = balanceAfter;
    }
}
