package com.bidy.chat.dto;

import com.bidy.chat.entity.ChatRoomEntity;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoomDto {
    private Long id;
    private Long productId;
    private String productName;
    private String productPostName;
    private Long sellerId;
    private String sellerName;
    private Long buyerId;
    private String buyerName;
    private String lastMessage;
    private LocalDateTime lastMessageTime;
    private LocalDateTime createdAt;

    public static ChatRoomDto from(ChatRoomEntity entity) {
        if (entity == null) {
            return null;
        }

        return ChatRoomDto.builder()
                .id(entity.getId())
                .productId(entity.getProduct().getProductId())
                .productName(entity.getProduct().getPostName())
                .productPostName(entity.getProduct().getPostName())
                .sellerId(entity.getSeller().getMemberId())
                .sellerName(entity.getSeller().getMemberNickname())
                .buyerId(entity.getBuyer().getMemberId())
                .buyerName(entity.getBuyer().getMemberNickname())
                .lastMessage(entity.getLastMessage())
                .lastMessageTime(entity.getLastMessageTime())
                .createdAt(entity.getCreatedAt())
                .build();
    }
}