package com.bidy.chat.service;

import com.bidy.chat.dto.ChatMessageDto;
import com.bidy.chat.dto.ChatRoomDto;
import com.bidy.chat.dto.ChatRoomListDto;
import com.bidy.chat.entity.ChatMessageEntity;
import com.bidy.chat.entity.ChatRoomEntity;
import com.bidy.chat.repository.ChatMessageRepository;
import com.bidy.chat.repository.ChatRoomRepository;
import com.bidy.member.domain.Member;
import com.bidy.member.repository.MemberRepository;
import com.bidy.post.domain.Product;
import com.bidy.post.repository.PostProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final MemberRepository memberRepository;
    private final  PostProductRepository postProductRepository;

    // 기존 채팅방 있으면 반환, 없으면 생성
    public ChatRoomDto getOrCreateRoom(Long productId, Long loginId) {
        // 경매 상품
        Product product = postProductRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다."));
        // 판매자
        Member seller = product.getUser();
        // 구매자
        Member buyer = memberRepository.findById(loginId)
                .orElseThrow(() -> new IllegalArgumentException("구매자 정보가 없습니다."));

        ChatRoomEntity room = chatRoomRepository
                .findByProductAndSellerAndBuyerIds(product, seller.getMemberId(), buyer.getMemberId())
                .orElseGet(() -> chatRoomRepository.save(ChatRoomEntity.builder()
                        .product(product)
                        .seller(seller)
                        .buyer(buyer)
                        .createdAt(LocalDateTime.now())
                        .build()));

        // Entity → DTO 변환
        return ChatRoomDto.builder()
                .id(room.getId())
                .productId(product.getProductId())
                .productName(product.getPostName())
                .productPostName(product.getPostName())
                .sellerId(room.getSeller() != null ? room.getSeller().getMemberId() : null)
                .sellerName(room.getSeller() != null ? room.getSeller().getMemberNickname() : null)
                .buyerId(room.getBuyer() != null ? room.getBuyer().getMemberId() : null)
                .buyerName(room.getBuyer() != null ? room.getBuyer().getMemberNickname() : null)
                .lastMessage(room.getLastMessage())
                .lastMessageTime(room.getLastMessageTime())
                .createdAt(room.getCreatedAt())
                .build();
    }

    // 특정 채팅방 메시지 목록 가져오기
    public List<ChatMessageDto> getMessages(Long roomId) {
        ChatRoomEntity room = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("채팅방을 찾을 수 없습니다."));

        List<ChatMessageEntity> messages = chatMessageRepository
                .findByChatRoomOrderByCreatedAtAsc(room);

        // Entity List → DTO List 변환
        return messages.stream()
                .map(ChatMessageDto::from)
                .collect(Collectors.toList());
    }

    // 메시지 저장
    @Transactional
    public ChatMessageDto saveMessage(ChatMessageDto dto) {
        // 채팅방 조회
        ChatRoomEntity room = chatRoomRepository.findById(dto.getRoomId())
                .orElseThrow(() -> new IllegalArgumentException("채팅방을 찾을 수 없습니다."));

        // 발신자 조회
        Member sender = memberRepository.findById(dto.getSenderId())
                .orElseThrow(() -> new IllegalArgumentException("발신자를 찾을 수 없습니다."));

        // 권한 검증
        if (!room.getSeller().getMemberId().equals(sender.getMemberId()) &&
                !room.getBuyer().getMemberId().equals(sender.getMemberId())) {
            throw new IllegalArgumentException("이 채팅방에 참여할 권한이 없습니다.");
        }

        // DTO → Entity 변환
        ChatMessageEntity message = ChatMessageEntity.builder()
                .chatRoom(room)
                .sender(sender)
                .message(dto.getMessage())
                .messageType(dto.getMessageType())
                .createdAt(LocalDateTime.now())
                .isRead(false)
                .build();

        // 저장
        ChatMessageEntity saved = chatMessageRepository.save(message);

        // 채팅방 마지막 메시지 업데이트
        room.setLastMessage(saved.getMessage());
        room.setLastMessageTime(saved.getCreatedAt());
        chatRoomRepository.save(room);

        // Entity → DTO 변환
        return ChatMessageDto.from(saved);
    }

    // 회원의 채팅방 리스트 가져오기
    public List<ChatRoomListDto> getMyChatRooms(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));

        List<ChatRoomEntity> rooms = chatRoomRepository.findByMemberWithFetch(member);

        // Entity List → DTO List 변환
        return rooms.stream()
                .map(room -> ChatRoomListDto.from(room, memberId))
                .collect(Collectors.toList());
    }

    // 채팅방 상세 정보
    public ChatRoomDto getChatRoomById(Long roomId) {
        ChatRoomEntity room = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("채팅방을 찾을 수 없습니다."));

        return ChatRoomDto.from(room);
    }

}