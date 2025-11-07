package com.bidy.chat.controller;

import com.bidy.chat.entity.ChatMessageEntity;
import com.bidy.chat.entity.ChatRoomEntity;
import com.bidy.chat.service.ChatService;
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

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    // 채팅방 목록
    @GetMapping("/list")
    public String chatList(@RequestParam Long userId, Model model) {
        List<ChatRoomEntity> rooms = chatService.getMyChatRooms(userId);
        model.addAttribute("rooms", rooms);
        model.addAttribute("userId", userId);
        return "chat-list";
    }

    // 게시글에서 '채팅하기' 클릭 시 채팅방으로
    @GetMapping("/{auctionId}")
    public String enterChat(@PathVariable Long auctionId,
                            @RequestParam Long sellerId,
                            @RequestParam Long buyerId,
                            Model model) {
        // 기존 채팅 있으면 가져옴. 없으면 새 채팅방 생성
        ChatRoomEntity room = chatService.getOrCreateRoom(auctionId, sellerId, buyerId);
        // 채팅 이력 가져오기
        List<ChatMessageEntity> messages = chatService.getMessages(room.getId());

        model.addAttribute("room", room);
        model.addAttribute("messages", messages);
        model.addAttribute("buyerId", buyerId);
        return "chat"; // chat.jsp
    }

    // 메시지 전송
    @MessageMapping("/chat/{roomId}")
    @SendTo("/topic/public/{roomId}")
    public ChatMessageEntity sendMessage(
            @DestinationVariable Long roomId,
            @Payload ChatMessageEntity chatMessage) {
        chatMessage.setCreatedAt(LocalDateTime.now());
        chatService.saveMessage(chatMessage);
        return chatMessage;
    }
}


