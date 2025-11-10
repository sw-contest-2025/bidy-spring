package com.bidy.chat.repository;

import com.bidy.chat.entity.ChatRoomEntity;
import com.bidy.member.domain.Member;
import com.bidy.post.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoomEntity,Long> {
    // 해당 경매에서 판매자와 구매자가 속한 채팅방 찾기
    @Query("SELECT cr FROM ChatRoomEntity cr " +
            "WHERE cr.product = :product AND cr.seller.memberId = :sellerId AND cr.buyer.memberId = :buyerId")
    Optional<ChatRoomEntity> findByProductAndSellerAndBuyerIds(@Param("product") Product product,
                                                               @Param("sellerId") Long sellerId,
                                                               @Param("buyerId") Long buyerId);

    @Query("SELECT cr FROM ChatRoomEntity cr " +
            "LEFT JOIN FETCH cr.seller " +
            "LEFT JOIN FETCH cr.buyer " +
            "WHERE cr.seller = :member OR cr.buyer = :member " +
            "ORDER BY cr.lastMessageTime DESC")
    List<ChatRoomEntity> findByMemberWithFetch(@Param("member") Member member);
}
