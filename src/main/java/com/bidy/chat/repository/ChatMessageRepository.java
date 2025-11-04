package com.bidy.chat.repository;

import com.bidy.chat.entity.ChatMessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessageEntity,Long> {
    List<ChatMessageEntity> findByChatRoomIdOrderByCreatedAtAsc(Long roomId);
}
