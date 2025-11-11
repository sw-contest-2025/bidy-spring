package com.bidy.auction.service;

import com.bidy.auction.domain.Bid;
import com.bidy.auction.repository.BidRepository;
import com.bidy.member.domain.Member;
import com.bidy.notification.domain.Notification;
import com.bidy.notification.repository.NotificationRepository;
import com.bidy.post.domain.Product;
import com.bidy.post.repository.PostProductRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class AuctionScheduler {
    private final PostProductRepository productRepository;
    private final NotificationRepository notificationRepository;
    private final BidRepository bidRepository;

    public AuctionScheduler(
            PostProductRepository productRepository,
            NotificationRepository notificationRepository,
            BidRepository bidRepository,
            AuctionService auctionService)
    {
        this.productRepository = productRepository;
        this.notificationRepository = notificationRepository;
        this.bidRepository = bidRepository;
    }

    // 1분마다 경매 종료 여부 확인
    @Scheduled(fixedRate = 60000)
    @Transactional
    public void checkAuctionEndings()
    {
        // 1. 모든 진행 중인 상품 조회
        List<Product>  products = productRepository.findAll();
        LocalDateTime now = LocalDateTime.now();

        for(Product product : products){
            LocalDateTime endTime = product.calculateEndTime();
            // 2. 경매가 종료되었는지 확인
            if(endTime != null && endTime.isBefore(now)){
                // 3. 해당 상품에 대한 입찰자 찾기
                List<Bid> bids = bidRepository.findByProduct(product);

                Set<Member> allBidders = bids.stream()
                        .map(Bid::getBidder)
                        .collect(Collectors.toSet()); // 중복 입찰자 제거
                // 4. 낙찰자 결정 및 알림
                Optional<Bid> winningBid = bidRepository.findTopByProductOrderByBidTimeDesc(product);
                if(winningBid.isPresent()){
                    Member winner = winningBid.get().getBidder();

                    // 낙찰자에게 알림
                    sendNotification(winner, product, Notification.NotificationType.AUCTION_WON, product.getPostName() + "상품에 낙찰되셨습니다!");
                    // 다른 입찰자들에게 알림
                    allBidders.stream()
                            .filter(member -> !member.getMemberId().equals(winner.getMemberId()))
                            .forEach(member -> sendNotification(member, product, Notification.NotificationType.AUCTION_ENDED, product.getPostName() + "경매가 종료되었습니다."));
                }
                product.setEnded(true);
                productRepository.save(product);
            }
        }
    }
    private void sendNotification(Member recipient, Product product, Notification.NotificationType type, String message){
        Notification notification = new Notification();
        notification.setRecipient(recipient);
        notification.setProduct(product);
        notification.setType(type);
        notification.setMessage(message);
        notificationRepository.save(notification);
    }
}