package com.bidy.chat.service;

import com.bidy.chat.entity.ChatMessageEntity;
import com.bidy.chat.entity.ChatRoomEntity;
import com.bidy.chat.repository.ChatMessageRepository;
import com.bidy.chat.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;

    public ChatRoomEntity getOrCreateRoom(Long auctionId, Long sellerId, Long buyerId) {
        return chatRoomRepository.findByAuctionIdAndBuyerIdAndSellerId(auctionId, sellerId, buyerId)
                .orElseGet(() -> chatRoomRepository.save(ChatRoomEntity.builder()
                        .auctionId(auctionId)
                        .sellerId(sellerId)
                        .buyerId(buyerId)
                        .createdAt(LocalDateTime.now())
                        .build()));
    }

    public List<ChatMessageEntity> getMessages(Long roomId) {
        return chatMessageRepository.findByChatRoomIdOrderByCreatedAtAsc(roomId);
    }

    public ChatMessageEntity saveMessage(ChatMessageEntity message) {
        return chatMessageRepository.save(message);
    }

    // 채팅방 리스트 가져오기
    public List<ChatRoomEntity> getMyChatRooms(Long userId) {
        return chatRoomRepository.findBySellerIdOrBuyerIdOrderByLastMessageTimeDesc(userId, userId);
    }

}
