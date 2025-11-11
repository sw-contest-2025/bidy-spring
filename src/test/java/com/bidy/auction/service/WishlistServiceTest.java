package com.bidy.auction.service;

import com.bidy.member.domain.Member;
import com.bidy.member.repository.MemberRepository;
import com.bidy.post.domain.Product;
import com.bidy.post.repository.PostProductRepository;
import com.bidy.wishlist.domain.Wishlist;
import com.bidy.wishlist.repository.WishlistRepository;
import com.bidy.wishlist.service.WishlistService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@Transactional
public class WishlistServiceTest {
    @Autowired
    private WishlistService wishlistService;
    @Autowired
    private WishlistRepository wishlistRepository;
    @Autowired
    private PostProductRepository postProductRepository;
    @Autowired
    private MemberRepository memberRepository;

    private Member TEST_MEMBER;
    private int TEST_PRODUCT_ID;
    private final Long NON_EXISTENT_ID = 99999L;

    @BeforeEach
    void setUp() {
        // 1. Member 초기화
        TEST_MEMBER = new Member();
        TEST_MEMBER.setMemberEmail("wish_tester@mail.com");
        TEST_MEMBER.setMemberNickname("찜테스터");
        TEST_MEMBER.setMemberBirthday(LocalDate.of(2000, 1, 1));
        TEST_MEMBER.setMemberName("찜 테스트 이름");
        TEST_MEMBER.setMemberPw("test1234");
        TEST_MEMBER.setMemberRole("MEMBER");
        memberRepository.save(TEST_MEMBER);

        // 2. Product 초기화 (찜 대상 상품)
        Product product = new Product();
        product.setUser(TEST_MEMBER);
        product.setPostName("Wish Test Product");
        product.setCategory("잡화");
        product.setMinPrice(1000);
        product.setDeliveryMethod("택배");
        product.setCurrentPrice(1000);
        product.setDurationDays(1);
        product.setDurationHours(0);
        product.setDurationMinutes(0);

        postProductRepository.save(product);
        TEST_PRODUCT_ID = product.getProductId().intValue();
    }
    @Test
    @DisplayName("성공: 찜 기록이 없을 때 토글 시 'added'를 반환하고 찜이 추가")
    void should_AddWishlist_When_NoRecordExists() {
        // given: 현재 찜 기록 없음

        // when
        String action = wishlistService.toggleWishlist(TEST_MEMBER, TEST_PRODUCT_ID);
        // then
        // 찜 기록이 DB에 생성되었는지 확인
        assertThat(wishlistRepository.existsByMemberMemberIdAndProductProductId(TEST_MEMBER.getMemberId(), (long) TEST_PRODUCT_ID)).isTrue();
        assertThat(action).isEqualTo("added");
    }

    @Test
    @DisplayName("성공: 찜 기록이 존재할 때 상태 조회 시 true 반환")
    void should_ReturnTrue_When_WishlistExists(){
        // given
        Product product = postProductRepository.findById((long) TEST_PRODUCT_ID).get();
        Wishlist existingWish = new Wishlist(TEST_MEMBER, product);
        wishlistRepository.save(existingWish);

        // when
        boolean status = wishlistService.getWishlistStatus(TEST_MEMBER.getMemberId(), TEST_PRODUCT_ID);

        // then
        assertThat(status).isTrue();
    }
}
