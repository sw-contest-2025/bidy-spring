package com.bidy.home.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "PRODUCT")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int productId;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 20)
    private String category;

    @Column(nullable = false)
    private int minPrice;

    @Column(name = "delivery_method", nullable = false, length = 20)
    private String deliveryMethod;

    private Integer durationDays;
    private Integer durationHours;
    private Integer durationMinutes;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "image_url", length = 255)
    private String imageUrl;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(columnDefinition = "INTEGER DEFAULT 0")
    private int views;

    @Column(nullable = false)
    private int currentPrice;


    /**
     * createdAt과 duration을 더하여
     * 경매 종료 시점을 계산하여 반환
     * @return 경매 종료 시각 (LocalDateTime)
     */
    public LocalDateTime calculateEndTime() {
        // 상품 등록이 안 된 상태
        if(this.getCreatedAt() == null) {
            return null;
        }

        int days = this.getDurationDays();
        int hours = this.getDurationHours();
        int minutes = this.getDurationMinutes();

        return this.getCreatedAt()
                .plusDays(days)
                .plusHours(hours)
                .plusMinutes(minutes);
    }
}
