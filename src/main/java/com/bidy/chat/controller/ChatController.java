package com.bidy.chat.controller;

import com.bidy.chat.dto.ChatMessageDto;
import com.bidy.chat.dto.ChatRoomDto;
import com.bidy.chat.dto.ChatRoomListDto;
import com.bidy.chat.entity.ChatMessageEntity;
import com.bidy.chat.entity.ChatRoomEntity;
import com.bidy.chat.service.ChatService;
import com.bidy.member.domain.Member;
import com.bidy.member.repository.MemberRepository;
import jakarta.servlet.http.HttpSession;
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

import java.nio.file.attribute.UserPrincipal;
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
    public String chatList(HttpSession session, Model model) {
        // 로그인 세션에서 회원 가져오기
        Member loginMember = (Member) session.getAttribute("member");
        if (loginMember == null) {
            return "redirect:/login";
        }

        Long loginId = loginMember.getMemberId();
        List<ChatRoomListDto> rooms = chatService.getMyChatRooms(loginId);

        model.addAttribute("rooms", rooms);
        model.addAttribute("loginId", loginId);
        return "chat_list";
    }

    // 게시글에서 '채팅하기' 클릭 시 채팅방으로
    @GetMapping("/{productId}")
    public String enterChat(@PathVariable Long productId,
                            HttpSession session,
                            Model model) {
        // 세션에서 로그인한 회원 가져오기
        Member loginMember = (Member) session.getAttribute("member");

        if (loginMember == null) { // 없으면 로그인페이지로
            return "redirect:/login";
        }
        Long loginId = loginMember.getMemberId();
        System.out.println("로그인한 회원 ID: " + loginId);

        // 채팅방 가져오기 또는 생성
        ChatRoomDto room = chatService.getOrCreateRoom(productId, loginId);
        // 채팅방 있을때-기존 메시지 내역 가져오기
        List<ChatMessageDto> messages = chatService.getMessages(room.getId());

        // 화면에 전달할 데이터 설정
        model.addAttribute("room", room);           // 채팅방 정보
        model.addAttribute("messages", messages);   // 메시지 목록
        model.addAttribute("loginId", loginId);     // 현재 사용자 ID

        System.out.println("Model에 추가된 loginId: " + loginId);

        return "chat";
    }

    // 판매자가 해당 채팅방으로 들어감
    @GetMapping("/room/{roomId}")
    public String chatFromList(@PathVariable Long roomId,
                               HttpSession session,
                               Model model) {
        Member loginMember = (Member) session.getAttribute("member");

        if (loginMember == null) { // 없으면 로그인페이지로
            return "redirect:/login";
        }
        Long loginId = loginMember.getMemberId();

        // roomId로 해당 채팅방 가져오기
        ChatRoomDto room = chatService.getChatRoomById(roomId);
        // 채팅방 있을때-기존 메시지 내역 가져오기
        List<ChatMessageDto> messages = chatService.getMessages(room.getId());

        model.addAttribute("room", room);           // 채팅방 정보
        model.addAttribute("messages", messages);   // 메시지 목록
        model.addAttribute("loginId", loginId);     // 현재 사용자 ID

        return "chat";
    }

    // 메시지 전송
    @MessageMapping("/chat/{roomId}")
    @SendTo("/topic/public/{roomId}")
    public ChatMessageDto sendMessage(
            @DestinationVariable Long roomId,
            @Payload ChatMessageDto dto) {

        // roomId 설정
        dto.setRoomId(roomId);

        // 메시지 저장, DTO 반환
        return chatService.saveMessage(dto);
    }
}


