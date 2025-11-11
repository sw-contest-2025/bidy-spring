package com.bidy.notification.service;

import com.bidy.notification.domain.Notification;
import com.bidy.notification.repository.NotificationRepository;
import com.bidy.post.domain.Product;
import com.bidy.post.repository.PostProductRepository;
import com.bidy.member.domain.Member;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final PostProductRepository productRepository;

    public NotificationService(NotificationRepository notificationRepository,
                               PostProductRepository productRepository) {
        this.notificationRepository = notificationRepository;
        this.productRepository = productRepository;
    }

    @Transactional(readOnly = true)
    public List<Notification> getUnreadNotifications(Long memberId) {
        return notificationRepository.findByRecipientMemberIdAndIsReadFalseOrderByCreatedAtDesc(memberId);
    }

    /**
     * 마감 1시간 전 알림 스케줄러
     * 경매 종료 후 낙찰되지 않은 입찰자에게 알림
     */
    @Scheduled(fixedRate = 60000) // 1분마다 실행
    public void notifyAuctionEndingSoon() {
        LocalDateTime now = LocalDateTime.now();
        List<Product> products = productRepository.findAll();

        for (Product product : products) {
            LocalDateTime endTime = product.calculateEndTime();
            if (endTime == null || product.isEnded()) continue;

            // 마감 1시간 전 범위 확인
            if (endTime.isAfter(now) && endTime.isBefore(now.plusMinutes(61))) {

                // 이미 알림 보냈는지 확인
                boolean alreadySent = notificationRepository.findAll()
                        .stream()
                        .anyMatch(n -> n.getProduct().equals(product)
                                && n.getType() == Notification.NotificationType.AUCTION_ENDED
                                && n.getMessage().contains("마감 1시간 전"));

                if (!alreadySent) {
                    Notification n = new Notification();
                    n.setRecipient(product.getUser()); // 판매자
                    n.setProduct(product);
                    n.setType(Notification.NotificationType.AUCTION_ENDED);
                    n.setMessage(product.getPostName() + " 상품이 경매 마감 1시간 전입니다!");
                    n.setCreatedAt(LocalDateTime.now());
                    n.setIsRead(false);
                    notificationRepository.save(n);
                }
            }
        }
    }
}
