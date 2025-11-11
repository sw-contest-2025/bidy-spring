package com.bidy.wishlist.repository;

import com.bidy.wishlist.domain.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WishlistRepository extends JpaRepository<Wishlist, Integer> {
    Optional<Wishlist> findByMemberMemberIdAndProductProductId(Long memberId, int productId);

    List<Wishlist> findByMember_MemberId(Long memberId);

    boolean existsByMemberMemberIdAndProductProductId(Long memberId, Long productId);

}
