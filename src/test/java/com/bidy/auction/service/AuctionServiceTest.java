package com.bidy.auction.service;

import com.bidy.auction.dto.BidRequestDto;
import com.bidy.auction.repository.BidRepository;
import com.bidy.post.domain.Product;
import com.bidy.home.repository.ProductRepository;
import com.bidy.member.domain.Member;
import com.bidy.member.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
public class AuctionServiceTest {
    @Autowired
    private AuctionService auctionService;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private BidRepository bidRepository;
    @Autowired
    private MemberRepository memberRepository;

    private Long TEST_PRODUCT_ID;
    private Long TEST_BIDDER_ID = 1L;
    private int INIT_CURRENT_PRICE = 10000;

    @BeforeEach
    void setUp(){
        // 1. Product 초기화
        Product product = new Product();
        product.setUser(null);
        product.setCurrentPrice(INIT_CURRENT_PRICE);
       // product.setCreatedAt(LocalDateTime.now().minusDays(1)); 알아서 자동생성 필요없음
        product.setDurationDays(2);
        product.setPostName("test product");
        product.setCategory("test category");
        product.setMinPrice(INIT_CURRENT_PRICE);
        product.setDurationHours(13);
        product.setDurationMinutes(30);
        product.setDescription("test product");
        product.setDeliveryMethod("택배");
        product.setImageUrl(null);
        productRepository.save(product);
        TEST_PRODUCT_ID = (long) product.getProductId().intValue();

        // 2. Member 초기화
        if(memberRepository.findByMemberNickname("테스터").isEmpty()) {
            Member testBidder = new Member();
            testBidder.setMemberEmail("test_user@mail.com");
            testBidder.setMemberPw("123");
            testBidder.setMemberName("테스터");
            testBidder.setMemberNickname("테스터");
            testBidder.setMemberRole("MEMBER");
            testBidder.setMemberBirthday(LocalDate.of(2000, 11, 6));

            memberRepository.save(testBidder);

            TEST_BIDDER_ID = testBidder.getMemberId();
        } else {
            TEST_BIDDER_ID = memberRepository.findByMemberNickname("테스터").get().getMemberId();
        }
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
        Product updatedProduct = productRepository.findById((long)TEST_PRODUCT_ID).orElseThrow();
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
}
