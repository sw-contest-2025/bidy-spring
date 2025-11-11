package com.bidy.wishlist.repository;

import com.bidy.wishlist.domain.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface WishlistRepository extends JpaRepository<Wishlist, Integer> {
    // 특정 회원의 특정 상품 찜 여부
    Optional<Wishlist> findByMemberMemberIdAndProductProductId(Long memberId, int productId);

    List<Wishlist> findByMember_MemberId(Long memberId);

    boolean existsByMemberMemberIdAndProductProductId(Long memberId, Long productId);

    // 회원의 모든 찜한 상품 ID 조회
    @Query("SELECT w.product.productId FROM Wishlist w WHERE w.member.memberId = :memberId")
    List<Long> findProductIdsByMemberId(@Param("memberId") Long memberId);
}
