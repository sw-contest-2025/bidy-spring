package com.bidy.chat.dto;

import com.bidy.chat.entity.ChatMessageEntity;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageDto {
    private Long id;
    private Long roomId;
    private Long senderId;
    private String senderName;
    private String message;
    private String messageType;
    private LocalDateTime createdAt;
    private Boolean isRead;

    /**
     * Entity에서 DTO로 변환하는 정적 팩토리 메서드
     * Mapper 클래스 불필요!
     */
    public static ChatMessageDto from(ChatMessageEntity entity) {
        if (entity == null) {
            return null;
        }

        return ChatMessageDto.builder()
                .id(entity.getId())
                .roomId(entity.getChatRoom().getId())
                .senderId(entity.getSender().getMemberId())
                .senderName(entity.getSender().getMemberNickname())
                .message(entity.getMessage())
                .messageType(entity.getMessageType())
                .createdAt(entity.getCreatedAt())
                .isRead(entity.getIsRead())
                .build();
    }
}