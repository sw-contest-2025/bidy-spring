package com.bidy.auction.service;

import com.bidy.auction.domain.Bid;
import com.bidy.auction.repository.BidRepository;
import com.bidy.home.domain.Product;
import com.bidy.home.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
public class AuctionService {
    private final BidRepository bidRepository;
    private final ProductRepository productRepository;

    // 생성자 주입
    public AuctionService(BidRepository bidRepository,  ProductRepository productRepository) {
        this.bidRepository = bidRepository;
        this.productRepository = productRepository;
    }

    /**
     * 특정 상품의 최신 입찰 기록 5개를 조회하는 메서드
     * @param productId 조회 대상 상품 id
     * @return id에 해당하는 상품
     */
    public Product findProductById(int productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new NoSuchElementException("Product not found"));
    }

    /**
     * 특정 상품의 최신 입찰 기록 5개를 조회하는 메서드
     * @param product 조회 대상 상품 엔티티
     * @return 최신 입찰 기록 5개 리스트
     */
    public List<Bid> getRecentBids(Product product) {
        return bidRepository.findTop5ByProductOrderByBidTimeDesc(product);
    }
}
