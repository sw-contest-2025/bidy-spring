package com.bidy.auction.service;

import com.bidy.auction.dto.BidRequestDto;
import com.bidy.auction.repository.BidRepository;
import com.bidy.notification.domain.Notification;
import com.bidy.notification.repository.NotificationRepository;
import com.bidy.post.domain.Product;
import com.bidy.member.domain.Member;
import com.bidy.member.repository.MemberRepository;
import com.bidy.post.repository.PostProductRepository;
import com.bidy.session.SessionConst;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
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
    @Mock
    private HttpSession mockSession;

    private Long TEST_PRODUCT_ID;
    private Member TEST_BIDDER_MEMBER;
    private Member OTHER_BIDDER_MEMBER;
    private int INIT_CURRENT_PRICE = 10000;
    private final Long NON_EXISTENT_ID = 99999L;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
        // 1. Member 초기화
        TEST_BIDDER_MEMBER = memberRepository.findByMemberNickname("테스터").orElseGet(() -> {
            Member m = new Member();
            m.setMemberEmail("test_user@mail.com");
            m.setMemberPw("123");
            m.setMemberName("테스터");
            m.setMemberNickname("테스터");
            m.setMemberRole("MEMBER");
            m.setMemberBirthday(LocalDate.of(2000, 11, 6));
            return memberRepository.save(m);
        });

        // 2. OTHER_BIDDER 초기화
        OTHER_BIDDER_MEMBER = memberRepository.findByMemberNickname("bidder2").orElseGet(() -> {
            Member m = new Member();
            m.setMemberEmail("member2@mail.com");
            m.setMemberPw("456");
            m.setMemberName("두번째입찰자");
            m.setMemberNickname("bidder2");
            m.setMemberRole("MEMBER");
            m.setMemberBirthday(LocalDate.of(2001, 1, 1));
            return memberRepository.save(m);
        });

        Mockito.when(mockSession.getAttribute(SessionConst.LOGIN_MEMBER))
                .thenReturn(TEST_BIDDER_MEMBER);

        // 3. Product 초기화
        Product product = new Product();
        product.setUser(TEST_BIDDER_MEMBER);
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
        BidRequestDto dto = new BidRequestDto(TEST_PRODUCT_ID, newBidPrice, 0L);
        // when
        String successMessage = auctionService.createBid(dto, mockSession);
        // then
        // 현재 최고가가 갱신되었는지 확인
        Product updatedProduct = postProductRepository.findById(TEST_PRODUCT_ID).orElseThrow();
        assertThat(updatedProduct.getCurrentPrice()).isEqualTo(newBidPrice);

        // Bid 기록이 하나 추가되었는지 확인
        // (Service 내에서 세션의 TEST_BIDDER_MEMBER가 입찰자로 사용되었는지 확인)
        assertThat(bidRepository.count()).isGreaterThanOrEqualTo(1);
    }

    // 2. 실패 테스트: 낮은 금액 입찰 시 예외 발생 확인
    @Test
    @DisplayName("실패: 현재 최고가와 같거나 낮은 금액으로 입찰 시 예외 발생")
    void should_ThrowException_When_LowerOrSamePriceIsGiven() {
        // given
        int samePrice = INIT_CURRENT_PRICE;
        BidRequestDto dto = new BidRequestDto(TEST_PRODUCT_ID, samePrice, 0L);
        // when, then
        assertThrows(IllegalStateException.class, () -> {
            auctionService.createBid(dto, mockSession);
        }, "최고가보다 높게 입찰해야 합니다.");
    }

    @Test
    @DisplayName("성공: 최고가 갱신 시 이전 입찰자에게 알림이 생성")
    void should_CreateNotification_When_BidIsCrossed(){
        // given: TEST_BIDDER_MEMBER.getMemberId()가 최초 입찰자
        Long initialBidderId = TEST_BIDDER_MEMBER.getMemberId();

        // 1. TEST_BIDDER가 최초 입찰 (mockSession에 TEST_BIDDER가 Mocking 되어있음)
        BidRequestDto initialDto = new BidRequestDto(TEST_PRODUCT_ID, INIT_CURRENT_PRICE + 100, 0L);
        auctionService.createBid(initialDto, mockSession);

        // 2. OTHER_BIDDER로 세션 변경 Mocking
        Mockito.when(mockSession.getAttribute(SessionConst.LOGIN_MEMBER))
                .thenReturn(OTHER_BIDDER_MEMBER);

        // 3. OTHER_BIDDER가 가격 갱신 시도
        int newPrice = INIT_CURRENT_PRICE + 500;
        BidRequestDto crossingDto = new BidRequestDto(TEST_PRODUCT_ID, newPrice, 0L);
        auctionService.createBid(crossingDto, mockSession);

        // Then: 최초 입찰자(initialBidderId)에게 알림이 생성되었는지 확인
        List<Notification> notifications = notificationRepository.findByRecipientMemberIdAndIsReadFalseOrderByCreatedAtDesc(initialBidderId);

        assertThat(notifications.size()).isEqualTo(1);
        assertThat(notifications.get(0).getType()).isEqualTo(Notification.NotificationType.BID_CROSSED);
    }

    @Test
    @DisplayName("실패: 존재하지 않는 상품 ID로 입찰 시 NoSuchElementException 발생")
    void should_ThrowException_When_ProductNotFound() {
        // given
        int newBidPrice = INIT_CURRENT_PRICE + 100;
        BidRequestDto dto = new BidRequestDto(NON_EXISTENT_ID, newBidPrice, 0L);

        // when, then
        assertThrows(NoSuchElementException.class, () -> {
            auctionService.createBid(dto, mockSession);
        }, "존재하지 않는 상품입니다.");
    }

    @Test
    @DisplayName("실패: 세션에 로그인 정보가 없을 경우 (Service에서 IllegalStateException 발생 가정)")
    void should_ThrowException_When_NotLoggedIn() {
        // given
        Mockito.when(mockSession.getAttribute(SessionConst.LOGIN_MEMBER))
                .thenReturn(null);

        int newBidPrice = INIT_CURRENT_PRICE + 100;
        BidRequestDto dto = new BidRequestDto(TEST_PRODUCT_ID, newBidPrice, 0L);

        // when, then
        assertThrows(IllegalStateException.class, () -> {
            auctionService.createBid(dto, mockSession);
        }, "로그인이 필요합니다."); // Service에서 던지는 예외 메시지
    }
}