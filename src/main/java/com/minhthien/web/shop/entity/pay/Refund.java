package com.minhthien.web.shop.entity.pay;

import com.minhthien.web.shop.enums.RefundStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "refunds")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Refund {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long orderId;
    private BigDecimal amount;
    private String reason;

    @Enumerated(EnumType.STRING)
    private RefundStatus status; // REQUESTED | COMPLETED | REJECTED

    private LocalDateTime createdAt;
    private LocalDateTime completedAt;

    @PrePersist
    void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.status = RefundStatus.REQUESTED;
    }
}
