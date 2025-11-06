package com.bidy.home.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int productId;
    @Column(
            length = 50,
            unique = true,
            nullable = false
    )
    private String name;
    private Timestamp createAt;
    private int durationDays;
    private int durationHours;
    private int durationMinutes;
    private int currentPrice;


    /**
     * createdAt과 duration을 더하여
     * 경매 종료 시점을 계산하여 반환
     * @return 경매 종료 시각 (LocalDateTime)
     */
    public LocalDateTime calculateEndTime() {
        // 상품 등록이 안 된 상태
        if(this.getCreateAt() == null) {
            return null;
        }

        int days = this.getDurationDays();
        int hours = this.getDurationHours();
        int minutes = this.getDurationMinutes();

        return this.getCreateAt()
                .toLocalDateTime()
                .plusDays(days)
                .plusHours(hours)
                .plusMinutes(minutes);
    }
}
