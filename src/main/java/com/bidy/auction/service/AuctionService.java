package com.bidy.auction.service;

import com.bidy.auction.domain.Bid;
import com.bidy.auction.dto.BidRequestDto;
import com.bidy.auction.repository.BidRepository;
import com.bidy.auction.repository.ItemTagRepository;
import com.bidy.notification.domain.Notification;
import com.bidy.notification.repository.NotificationRepository;
import com.bidy.post.domain.Product;
import com.bidy.member.domain.Member;
import com.bidy.member.repository.MemberRepository;
import com.bidy.post.repository.PostProductRepository;
import com.bidy.session.SessionConst;
import jakarta.servlet.http.HttpSession;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.awt.print.Pageable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly=true)
public class AuctionService {
    private final BidRepository bidRepository;
    private final PostProductRepository productRepository;
    private final MemberRepository memberRepository;
    private final NotificationRepository notificationRepository;
    private final ItemTagRepository itemTagRepository;
    private static final int RECOMMENDED_COUNT = 3;

    // 생성자 주입
    public AuctionService(
            BidRepository bidRepository,
            PostProductRepository productRepository,
            MemberRepository memberRepository,
            NotificationRepository notificationRepository,
            ItemTagRepository itemTagRepository) {
        this.bidRepository = bidRepository;
        this.productRepository = productRepository;
        this.memberRepository = memberRepository;
        this.notificationRepository = notificationRepository;
        this.itemTagRepository = itemTagRepository;
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
     * @param dto 입찰 요청 데이터
     * @return 입찰 성공 메시지
     * @throws NoSuchElementException 상품 또는 입찰자 ID가 존재하지 않을 경우
     * @throws IllegalStateException 경매 종료, 입찰가 미달, 연속 입찰 시도, 혹은 100원 단위가 아닐 경우
     */
    @Transactional
    public String createBid(BidRequestDto dto, HttpSession session){
        //유효성 검증 (Product)
        Product product = productRepository.findById((long)Math.toIntExact(dto.getProductId()))
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 상품입니다."));

        //유효성 검증 (Member)
        Member bidder = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);
        if (bidder == null) {
            return "redirect:/login";
        }

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

        // 2-1. 100원 단위 입찰 검증
        if (dto.getBidPrice() % 100 != 0) {
            throw new IllegalStateException("입찰 금액은 100원 단위로만 가능합니다.");
        }

        // 3. 연속 입찰 금지 검증
        if(previousBid.isPresent()){
            Long oldBidderId = previousBid.get().getBidder().getMemberId();
            if(oldBidderId.equals(bidder.getMemberId())){
                throw new IllegalStateException("현재 최고가 입찰자는 연속 입찰할 수 없습니다.");
            }
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
        return "입찰이 성공적으로 완료되었습니다! 현재 당신이 최고가입니다.";
    }


    /**
     * 관련 태그 기반 상품 3개를 추천하고, 부족하면 조회수 순으로 채우는 메서드
     * @param currentProductId 현재 보고 있는 상품 ID
     * @return 추천 상품 3개 리스트
     */
    @Transactional(readOnly = true)
    public List<Product> getRecommendProducts(Long currentProductId){
        // 1. 현재 상품과 관련된 태그 ID 목록 조회
        List<Long> tagIds = itemTagRepository.findTagIdsByProductId(currentProductId);

        // 2. 같은 태그를 가진 추천 상품 목록 조회
        List<Product> recommendedList = new ArrayList<>();

        if(!tagIds.isEmpty()){
            // Tag ID 목록 기반으로 상품 조회
            recommendedList = itemTagRepository.findProductsByTagIdsExcludingProduct(
                    tagIds, currentProductId, PageRequest.of(0, RECOMMENDED_COUNT)
            );
        }

        // 3. 부족한 추천 상품은 조회수 기반으로 채움
        int currentSize = recommendedList.size();
        if(currentSize < RECOMMENDED_COUNT){
            int needMore = RECOMMENDED_COUNT - currentSize;

            List<Long> excludedProductIds = recommendedList.stream()
                    .map(Product::getProductId)
                    .collect(Collectors.toList());
            excludedProductIds.add(currentProductId);

            List<Product> topViewedProducts = productRepository.findByViewsExcludingIds(
                    excludedProductIds,
                    (Pageable) PageRequest.of(0, needMore)
            );
            recommendedList.addAll(topViewedProducts);
        }
        return recommendedList.stream()
                .limit(RECOMMENDED_COUNT)
                .collect(Collectors.toList());
    }
}