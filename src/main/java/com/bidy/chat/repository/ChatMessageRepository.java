package com.bidy.chat.repository;

import com.bidy.chat.entity.ChatMessageEntity;
import com.bidy.chat.entity.ChatRoomEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessageEntity,Long> {
    // 한 채팅방에 대한 메시지를 올림차순(오래된 순서)으로 가져오기
    List<ChatMessageEntity> findByChatRoomOrderByCreatedAtAsc(ChatRoomEntity chatRoom);
}
