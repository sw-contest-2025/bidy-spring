package com.bidy.chat.service;

import com.bidy.chat.entity.ChatMessageEntity;
import com.bidy.chat.entity.ChatRoomEntity;
import com.bidy.chat.repository.ChatMessageRepository;
import com.bidy.chat.repository.ChatRoomRepository;
import com.bidy.member.domain.Member;
import com.bidy.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final MemberRepository memberRepository;

    // 기존 채팅방 있으면 반환, 없으면 생성
    public ChatRoomEntity getOrCreateRoom(Long auctionId, Long sellerId, Long buyerId) {
        // Member 엔티티 조회
        Member seller = memberRepository.findById(sellerId)
                .orElseThrow(() -> new IllegalArgumentException("판매자 정보가 없습니다."));
        Member buyer = memberRepository.findById(buyerId)
                .orElseThrow(() -> new IllegalArgumentException("구매자 정보가 없습니다."));

        return chatRoomRepository
                .findByAuctionIdAndSellerAndBuyer(auctionId, seller, buyer)
                .orElseGet(() -> chatRoomRepository.save(ChatRoomEntity.builder()
                        .auctionId(auctionId)
                        .seller(seller)
                        .buyer(buyer)
                        .createdAt(LocalDateTime.now())
                        .build()));
    }

    // 특정 채팅방 메시지 목록 가져오기
    public List<ChatMessageEntity> getMessages(ChatRoomEntity room) {
        return chatMessageRepository.findByChatRoomOrderByCreatedAtAsc(room);
    }

    // 메시지 저장
//    @Transactional
    public ChatMessageEntity saveMessage(ChatMessageEntity message) {
        // 메시지 저장
        ChatMessageEntity saved = chatMessageRepository.save(message);

        // 채팅방의 마지막 메시지 정보 업데이트
        ChatRoomEntity room = saved.getChatRoom();
        room.setLastMessage(saved.getMessage());
        room.setLastMessageTime(saved.getCreatedAt());

        chatRoomRepository.save(room);

        return saved;
    }

    // 회원의 채팅방 리스트 가져오기
    public List<ChatRoomEntity> getMyChatRooms(Long memberId) {
        // member있는지 확인
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));

        return chatRoomRepository.findByMemberWithFetch(member);
    }

    // 해당 채팅방 가져오기
    public ChatRoomEntity getChatRoomById(Long roomId) {
        return chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("채팅방을 찾을 수 없습니다."));
    }

}
