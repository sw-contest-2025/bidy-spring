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
    Optional<ChatRoomEntity> findByProductAndBuyer(Product product, Member buyer);

    // 회원이 판매자/구매자인 채팅방을 최근 메시지 순으로 가져오기
//    List<ChatRoomEntity> findBySellerOrBuyerOrderByLastMessageTimeDesc(
//            Member seller, Member buyer
//    );
    @Query("SELECT cr FROM ChatRoomEntity cr " +
            "LEFT JOIN FETCH cr.seller " +
            "LEFT JOIN FETCH cr.buyer " +
//            "LEFT JOIN FETCH cr.auction " +
            "WHERE cr.seller = :member OR cr.buyer = :member " +
            "ORDER BY cr.lastMessageTime DESC")
    List<ChatRoomEntity> findByMemberWithFetch(@Param("member") Member member);
}
