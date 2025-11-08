package com.bidy.chat.controller;

import com.bidy.chat.dto.ChatMessageDto;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;

@Controller
@RequestMapping("/test")
public class ChatTestController {

    // 테스트 채팅방 진입 (DB 조회 없이)
    @GetMapping("/chat")
    public String testChatRoom(@RequestParam(defaultValue = "1") Long roomId,
                               @RequestParam(defaultValue = "1") Long userId,
                               Model model) {
        model.addAttribute("roomId", roomId);
        model.addAttribute("userId", userId);
        return "chatRoom";  // chatRoom.html
    }

    // 테스트용 메시지 전송 (DB 저장 없이 바로 브로드캐스트)
    @MessageMapping("/test/chat/{roomId}")
    @SendTo("/topic/room/{roomId}")
    public ChatMessageDto sendTestMessage(
            @DestinationVariable Long roomId,
            @Payload ChatMessageDto message) {

        // 현재 시간 추가 (선택사항)
        System.out.println("테스트 메시지 수신: " + message.getMessage());

        // DB 저장 없이 바로 반환 (구독자들에게 전송됨)
        return message;
    }
}
