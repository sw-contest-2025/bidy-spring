package com.bidy.wishlist.service;

import com.bidy.post.repository.PostProductRepository;
import com.bidy.wishlist.domain.Wishlist;
import com.bidy.wishlist.repository.WishlistRepository;
import com.bidy.member.domain.Member;
import com.bidy.member.repository.MemberRepository;
import com.bidy.post.domain.Product;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Transactional
public class WishlistService {
    private final WishlistRepository wishlistRepository;
    private final MemberRepository memberRepository;
    private final PostProductRepository postProductRepository;

    public WishlistService(WishlistRepository wishlistRepository, MemberRepository memberRepository, PostProductRepository postProductRepository) {
        this.wishlistRepository = wishlistRepository;
        this.memberRepository = memberRepository;
        this.postProductRepository = postProductRepository;
    }

    /**
     * 특정 사용자의 특정 상품 위시리스트 상태 추가 또는 삭제
     * @param member 로그인된 사용자 Member 객체
     * @param productId 상품 ID
     * @return String - "added" 또는 "removed"
     */
    public String toggleWishlist(Member member, int productId){
        Long memberId = member.getMemberId(); // Member 객체에서 ID를 추출

        // 1. 기존 찜 목록 조회
        Optional<Wishlist> wishs = wishlistRepository.findByMemberMemberIdAndProductProductId(memberId, productId);

        //  2. Product 엔티티를 미리 조회
        Product product = postProductRepository.findById((long) productId)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 상품입니다."));

        // 3. 토글 로직
        if(wishs.isPresent()){
            // 3-1. 이미 찜 기록이 있다면 삭제
            wishlistRepository.delete(wishs.get());
            return "removed";
        }
        else{
            // 3-2. 찜 기록이 없다면 생성
            Wishlist newWish =  new Wishlist(member, product);
            wishlistRepository.save(newWish);
            return "added";
        }
    }

    public boolean getWishlistStatus(Long memberId, int productId){
        return wishlistRepository.existsByMemberMemberIdAndProductProductId(memberId, (long)productId);
    }
}
