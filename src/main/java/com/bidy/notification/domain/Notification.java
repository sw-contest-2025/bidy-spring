package com.bidy.notification.domain;

import com.bidy.member.domain.Member;
import com.bidy.post.domain.Product;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name="notification")
public class Notification {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long notiId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipient_id", nullable = false)
    private Member recipient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @Enumerated
    @Column(nullable = false)
    private NotificationType type;

    @Column(nullable = false, length = 255)
    private String message;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    private boolean isRead = false;

    public enum NotificationType{
        BID_CROSSED, // 최고가 갱신 알림
        AUCTION_WON, // 낙찰 완료 알림
        AUCTION_ENDED // 경매 종료 알림
    }


    public boolean getIsRead() {
        return isRead;
    }
    public void setIsRead(boolean isRead) {
        this.isRead = isRead;
    }


    public Long getNotiId() { return notiId; }
    public void setNotiId(Long notificationId) { this.notiId = notiId; }

    public Member getRecipient() { return recipient; }
    public void setRecipient(Member recipient) { this.recipient = recipient; }

    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }

    public NotificationType getType() { return type; }
    public void setType(NotificationType type) { this.type = type; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

}
