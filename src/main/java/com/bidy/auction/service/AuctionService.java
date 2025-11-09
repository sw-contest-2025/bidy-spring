package com.bidy.auction.service;

import com.bidy.auction.domain.Bid;
import com.bidy.auction.dto.BidRequestDto;
import com.bidy.auction.repository.BidRepository;
import com.bidy.notification.domain.Notification;
import com.bidy.notification.repository.NotificationRepository;
import com.bidy.post.domain.Product;
import com.bidy.home.repository.ProductRepository;
import com.bidy.member.domain.Member;
import com.bidy.member.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Transactional(readOnly=true)
public class AuctionService {
    private final BidRepository bidRepository;
    private final ProductRepository productRepository;
    private final MemberRepository memberRepository;
    private final NotificationRepository notificationRepository;

    // 생성자 주입
    public AuctionService(BidRepository bidRepository,  ProductRepository productRepository, MemberRepository memberRepository,  NotificationRepository notificationRepository) {
        this.bidRepository = bidRepository;
        this.productRepository = productRepository;
        this.memberRepository = memberRepository;
        this.notificationRepository = notificationRepository;
    }

    /**
     * 특정 상품의 최신 입찰 기록 5개를 조회하는 메서드
     * @param productId 조회 대상 상품 id
     * @return id에 해당하는 상품
     */
    public Product findProductById(int productId) {
        return productRepository.findById((long)productId)
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

    /**
     * 입찰하는 메서드
     * @param
     * @return
     */
    @Transactional
    public void createBid(BidRequestDto dto){
        //유효성 검증 (Product, Member)
        Product product = productRepository.findById((long)Math.toIntExact(dto.getProductId()))
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 상품입니다."));

        Member bidder = memberRepository.findById(dto.getBidderId())
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 입찰자 ID입니다."));

        Optional<Bid> previousBid = bidRepository.findTopByProductOrderByBidTimeDesc(product);

        // 경매 유효성 검증
        // 1. 마감 시간 검증
        LocalDateTime endTime = product.calculateEndTime();
        if(endTime != null && endTime.isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("경매가 이미 종료되었습니다.");
        }

        // 2. 금액 유효성 검증
        if(dto.getBidPrice() <= product.getCurrentPrice()){
            throw new IllegalStateException("최고가보다 높게 입찰해야 합니다.");
        }

        // 입찰 기록 저장
        Bid newBid = new Bid();
        newBid.setProduct(product);
        newBid.setBidder(bidder);
        newBid.setBidTime(LocalDateTime.now());
        newBid.setBidPrice(dto.getBidPrice());
        bidRepository.save(newBid);

        // Product currentPrice 갱신
        product.setCurrentPrice(dto.getBidPrice());
        productRepository.save(product);

        // 알림 생성 및 저장
        if(previousBid.isPresent() && !previousBid.get().getBidder().getMemberId().equals(bidder.getMemberId())) {
            Member oldBidder = previousBid.get().getBidder();

            // Notification 객체 생성
            Notification notification = new Notification();
            notification.setRecipient(oldBidder);
            notification.setProduct(product);
            notification.setType(Notification.NotificationType.BID_CROSSED);
            notification.setMessage(product.getPostName() + "상품의 최고가 입찰이 갱신되었습니다!");
            notification.setCreatedAt(LocalDateTime.now());
            notificationRepository.save(notification);
        }
    }
}