package com.bidy.post.domain;

import com.bidy.member.domain.Member;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "PRODUCT")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // ID 값 자동으로 생성 후 AUTO_INCREMENT
    private Long productId; // 상품 아이디

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false) // Product 테이블 컬럼 이름
    private Member user; // Member PK(member_id) 참조

    @Column(name = "post_name", nullable = false, length = 100)
    private String postName; // 작성글 제목

    @Column(nullable = false, length = 20)
    private String category ;

    @Column(nullable = false)
    private int minPrice; // 최소가격

    @Column(name = "delivery_method", nullable = false, length = 20)
    private String deliveryMethod;

    @Column(nullable = false)
    private int durationDays = 0;
    @Column(nullable = false)
    private int durationHours = 0;
    @Column(nullable = false)
    private int durationMinutes = 0;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "image_url", length = 255)
    private String imageUrl;

    @Transient
    private MultipartFile imageFile;// DB와 무관

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(columnDefinition = "INTEGER DEFAULT 0")
    private int views;

    @Column(nullable = false)
    private int currentPrice; // 현재가격
    @PrePersist
    public void prePersist() {
        if (currentPrice == 0) { //미리 값 초기화해주기
            currentPrice = minPrice;
        }
    }


    @Column(name = "is_ended", nullable = false)
    private boolean isEnded = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "winner_id", foreignKey = @ForeignKey(ConstraintMode.CONSTRAINT))
    private Member winner; // 경매 종료 시점에 설정 (진행 중엔 null)

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

    //경매 종료!!
    public void finishAuction(Member winner, int finalPrice) {
        this.isEnded = true;
        this.winner = winner;          // FK 세팅
        this.currentPrice = finalPrice;
        // finalPrice를 별도로 둘 거면 finalPrice에 넣고 currentPrice 유지도 가능
        // this.endedAt = LocalDateTime.now();
        // this.finalPrice = finalPrice;
    }

}
