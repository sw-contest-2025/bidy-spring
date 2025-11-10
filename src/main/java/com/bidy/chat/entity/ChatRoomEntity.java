package com.bidy.chat.entity;

import com.bidy.post.domain.Product;
import jakarta.persistence.*;
import lombok.*;
import com.bidy.member.domain.Member;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoomEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // 채팅방 고유 id
    private Long id;

    // 상품 참조
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id")
    // 판매자 회원 참조
    private Member seller;

    // 구매자 회원 참조
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "buyer_id")
    private Member buyer;

    // 채팅방 생성 시간
    private LocalDateTime createdAt;
    // 마지막 메시지
    private String lastMessage;
    // 마지막 메시지 시간
    private LocalDateTime lastMessageTime;
}