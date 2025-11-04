package com.bidy.chat.repository;

import com.bidy.chat.entity.ChatRoomEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoomEntity,Long> {
    Optional<ChatRoomEntity> findByAuctionIdAndBuyerId(Long auctionId, Long sellerId,Long buyerId);

    List<ChatRoomEntity> findBySellerIdOrBuyerIdOrderByLastMessageTimeDesc(
            Long sellerId, Long buyerId
    );
}
