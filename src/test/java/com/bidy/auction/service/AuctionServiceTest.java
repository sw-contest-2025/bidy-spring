package com.bidy.auction.service;

import com.bidy.auction.dto.BidRequestDto;
import com.bidy.auction.repository.BidRepository;
import com.bidy.notification.domain.Notification;
import com.bidy.notification.repository.NotificationRepository;
import com.bidy.post.domain.Product;
import com.bidy.member.domain.Member;
import com.bidy.member.repository.MemberRepository;
import com.bidy.post.repository.PostProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
public class AuctionServiceTest {
    @Autowired
    private AuctionService auctionService;
    @Autowired
    private PostProductRepository postProductRepository;
    @Autowired
    private BidRepository bidRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private NotificationRepository notificationRepository;

    private Long TEST_PRODUCT_ID;
    private Long TEST_BIDDER_ID = 1L;
    private Long OTHER_BIDDER_ID;
    private int INIT_CURRENT_PRICE = 10000;

    @BeforeEach
    void setUp(){
        // 1. Member 초기화
        Member testBidder;
        if(memberRepository.findByMemberNickname("테스터").isEmpty()) {
            testBidder = new Member();
            testBidder.setMemberEmail("test_user@mail.com");
            testBidder.setMemberPw("123");
            testBidder.setMemberName("테스터");
            testBidder.setMemberNickname("테스터");
            testBidder.setMemberRole("MEMBER");
            testBidder.setMemberBirthday(LocalDate.of(2000, 11, 6));

            memberRepository.save(testBidder);
            TEST_BIDDER_ID = testBidder.getMemberId();
        } else {
            testBidder = memberRepository.findByMemberNickname("테스터").get();
            TEST_BIDDER_ID = testBidder.getMemberId();
        }

        // 2. Product 초기화
        Product product = new Product();
        product.setUser(testBidder);
        product.setCurrentPrice(INIT_CURRENT_PRICE);
        product.setDurationDays(2);
        product.setPostName("test product");
        product.setCategory("test category");
        product.setMinPrice(INIT_CURRENT_PRICE);
        product.setDurationHours(13);
        product.setDurationMinutes(30);
        product.setDescription("test product");
        product.setDeliveryMethod("택배");
        product.setImageUrl(null);

        postProductRepository.save(product);
        TEST_PRODUCT_ID = product.getProductId();
    }

    // 1. 성공 테스트: 입찰 기록 저장 및 가격 갱신 확인
    @Test
    @DisplayName("성공: 최고가보다 높은 금액으로 입찰 시 기록이 저장되고 가격이 갱신")
    void should_PlaceBidAndRefreshPrice_When_HigherPriceIsGiven() {
        // given
        int newBidPrice = INIT_CURRENT_PRICE + 500;
        BidRequestDto dto = new BidRequestDto((long)TEST_PRODUCT_ID, newBidPrice, TEST_BIDDER_ID);

        // when
        auctionService.createBid(dto);

        // then
        // 현재 최고가가 갱신되었는지 확인
        Product updatedProduct = postProductRepository.findById(TEST_PRODUCT_ID).orElseThrow();
        assertThat(updatedProduct.getCurrentPrice()).isEqualTo(newBidPrice);

        // Bid 기록이 하나 추가되었는지 확인
        assertThat(bidRepository.count()).isGreaterThanOrEqualTo(1);
    }

    // 2. 실패 테스트: 낮은 금액 입찰 시 예외 발생 확인
    @Test
    @DisplayName("실패: 현재 최고가와 같거나 낮은 금액으로 입찰 시 예외 발생")
    void should_ThrowException_When_LowerOrSamePriceIsGiven() {
        // given
        int samePrice = INIT_CURRENT_PRICE;
        BidRequestDto dto = new BidRequestDto((long)TEST_PRODUCT_ID, samePrice, TEST_BIDDER_ID);

        // when, then
        assertThrows(IllegalStateException.class, () -> {
            auctionService.createBid(dto);
        }, "최고가보다 높게 입찰해야 합니다.");
    }

    @Test
    @DisplayName("성공: 최고가 갱신 시 이전 입찰자에게 알림이 생성")
    void should_CreateNotification_When_BidIsCrossed(){
        // member, product가 준비되어있다고 가정
        Long member1Id = 1L;
        Member member2 = new Member();
        member2.setMemberEmail("member2@mail.com");
        member2.setMemberPw("456");
        member2.setMemberName("두번째입찰자");
        member2.setMemberNickname("bidder2");
        member2.setMemberRole("MEMBER");
        member2.setMemberBirthday(LocalDate.of(2001, 1, 1));
        memberRepository.save(member2);
        Long member2Id = member2.getMemberId();

        // member1이 최초 입찰
        BidRequestDto initialDto = new BidRequestDto(TEST_PRODUCT_ID, INIT_CURRENT_PRICE + 100, member1Id);
        auctionService.createBid(initialDto);

        // member2가 가격 갱신 시도
        int newPrice = INIT_CURRENT_PRICE + 500;
        BidRequestDto crossingDto = new BidRequestDto((long)TEST_PRODUCT_ID, newPrice, member2Id);
        auctionService.createBid(crossingDto);

        // Then
        List<Notification> notifications = notificationRepository.findByRecipientMemberIdAndIsReadFalseOrderByCreatedAtDesc(member1Id);

        assertThat(notifications.size()).isEqualTo(1);
        assertThat(notifications.get(0).getType()).isEqualTo(Notification.NotificationType.BID_CROSSED);
    }

    @Test
    @DisplayName("실패: 존재하지 않는 상품 ID로 입찰 시 NoSuchElementException 발생")
    void should_ThrowException_When_ProductNotFound() {
        // given
        Long NON_EXISTENT_ID = 99999L;
        int newBidPrice = INIT_CURRENT_PRICE + 100;
        BidRequestDto dto = new BidRequestDto(NON_EXISTENT_ID, newBidPrice, OTHER_BIDDER_ID);

        // when, then
        assertThrows(NoSuchElementException.class, () -> {
            auctionService.createBid(dto);
        }, "상품을 찾을 수 없습니다.");
    }
}
