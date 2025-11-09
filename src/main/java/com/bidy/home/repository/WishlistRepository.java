package com.bidy.home.repository;

import com.bidy.home.domain.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WishlistRepository extends JpaRepository<Wishlist, Integer> {
    Optional<Wishlist> findByMemberMemberIdAndProductProductId(Long memberId, int productId);
}
