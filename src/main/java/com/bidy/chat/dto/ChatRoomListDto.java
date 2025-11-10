package com.bidy.chat.dto;

import com.bidy.chat.entity.ChatRoomEntity;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoomListDto {
    private Long id;
    private Long productId;
    private String productName;
    private String productPostName;
    private Long otherUserId;
    private String otherUserNickName;   // 상대방 이름
    private String lastMessage;
    private LocalDateTime lastMessageTime;

    public static ChatRoomListDto from(ChatRoomEntity entity, Long currentUserId) {
        if (entity == null) {
            return null;
        }

        // 현재 사용자가 seller인지 buyer인지 판단
        boolean isSeller = entity.getSeller().getMemberId().equals(currentUserId);
        String otherUserNickName = isSeller ?
                entity.getBuyer().getMemberNickname() :
                entity.getSeller().getMemberNickname();

        Long otherUserId = isSeller ?
                entity.getBuyer().getMemberId() :
                entity.getSeller().getMemberId();

        return ChatRoomListDto.builder()
                .id(entity.getId())
                .productId(entity.getProduct().getProductId())
                .productName(entity.getProduct().getPostName())
                .productPostName(entity.getProduct().getPostName())
                .otherUserId(otherUserId)
                .otherUserNickName(otherUserNickName)
                .lastMessage(entity.getLastMessage())
                .lastMessageTime(entity.getLastMessageTime())
                .build();
    }
}