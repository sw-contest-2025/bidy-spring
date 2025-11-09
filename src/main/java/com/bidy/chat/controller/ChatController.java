package com.bidy.chat.controller;

import com.bidy.chat.entity.ChatMessageEntity;
import com.bidy.chat.entity.ChatRoomEntity;
import com.bidy.chat.service.ChatService;
import com.bidy.member.domain.Member;
import com.bidy.member.repository.MemberRepository;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/chat")
public class ChatController {
    private final ChatService chatService;
    private final MemberRepository memberRepository;

    public ChatController(ChatService chatService, MemberRepository memberRepository) {
        this.chatService = chatService;
        this.memberRepository = memberRepository;
    }

    // 채팅방 목록
    @GetMapping("/list")
    public String chatList(@RequestParam Long memberId, Model model) {
        List<ChatRoomEntity> rooms = chatService.getMyChatRooms(memberId);
        model.addAttribute("rooms", rooms);
        model.addAttribute("memberId", memberId);
        return "chat_list";
    }

    // 게시글에서 '채팅하기' 클릭 시 채팅방으로
    @GetMapping("/{auctionId}")
    public String enterChat(@PathVariable Long auctionId,
                            @RequestParam Long sellerId,
                            @RequestParam Long buyerId,
                            Model model) {
        // 채팅방 가져오기 또는 생성
        ChatRoomEntity room = chatService.getOrCreateRoom(auctionId, sellerId, buyerId);
        // 채팅방 있을때-기존 메시지 내역 가져오기
        List<ChatMessageEntity> messages = chatService.getMessages(room);

        // 화면에 전달할 데이터 설정
        model.addAttribute("room", room);           // 채팅방 정보
        model.addAttribute("messages", messages);   // 메시지 목록
        model.addAttribute("buyerId", buyerId);     // 현재 사용자 ID

        return "chat"; // chat.jsp
    }

    // 메시지 전송
    @MessageMapping("/chat/{roomId}")
    @SendTo("/topic/public/{roomId}")
    public ChatMessageEntity sendMessage(
            @DestinationVariable Long roomId,
            @Payload ChatMessageEntity chatMessage,
            @RequestParam Long senderId) {

        // 채팅방 조회
        ChatRoomEntity room = chatService.getChatRoomById(roomId);

        // 발신자(문의자) 조회
        Member sender = memberRepository.findById(senderId)
                .orElseThrow(() -> new IllegalArgumentException("문의자를 찾을 수 없습니다."));

        // 메시지 엔티티 설정
        chatMessage.setChatRoom(room);
        chatMessage.setSender(sender);
        chatMessage.setCreatedAt(LocalDateTime.now());
        chatMessage.setIsRead(false);

        // 메시지 저장 & 채팅방 업데이트
        return chatService.saveMessage(chatMessage);
    }
}


