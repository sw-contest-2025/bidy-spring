package com.bidy.auction.repository;

import com.bidy.auction.domain.Bid;
import com.bidy.post.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BidRepository extends JpaRepository<Bid, Long> {
    // Bid 관련 비즈니스 요구사할

    // 해당 상품의 가장 높은 가격의 입찰 기록 조회
    Optional<Bid> findFirstByProductOrderByBidPriceDesc(Product product);
    Optional<Bid> findTopByProductOrderByBidTimeDesc(Product product);

    // 해당 상품의 최신 입찰 기록 5개 조회
    List<Bid> findTop5ByProductOrderByBidTimeDesc(Product product);

    List<Bid> findByProduct(Product product);

    List<Bid> findByBidder_MemberId(Long memberId);

}
