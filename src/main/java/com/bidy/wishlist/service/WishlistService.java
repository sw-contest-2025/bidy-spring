package com.bidy.wishlist.service;

import com.bidy.wishlist.domain.Wishlist;
import com.bidy.home.repository.ProductRepository;
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
    private final ProductRepository productRepository;

    public WishlistService(WishlistRepository wishlistRepository, MemberRepository memberRepository, ProductRepository productRepository) {
        this.wishlistRepository = wishlistRepository;
        this.memberRepository = memberRepository;
        this.productRepository = productRepository;
    }

    /**
     * 특정 사용자의 특정 상품 위시리스트 상태 추가 또는 삭제
     * @param memberId 사용자 ID
     * @param productId 상품 ID
     * @return boolean - true: 추가, false: 삭제
     */
    public boolean toggleWishlist(Long memberId, int productId){
        // 1. 기존 찜 목록 조회
        Optional<Wishlist> wishs = wishlistRepository.findByMemberMemberIdAndProductProductId(memberId, productId);

        if(wishs.isPresent()){
            // 2. 이미 찜 기록이 있다면 삭제
            wishlistRepository.delete(wishs.get());
            return false;
        }
        else{
            // 3. 찜 기록이 없다면 생성
            Member member = memberRepository.findById(memberId).get();
            Optional<Product> optionalProduct = productRepository.findById(productId);
            Product product = optionalProduct
                    .orElseThrow(() -> new NoSuchElementException("Product not found"));
            Wishlist newWish =  new Wishlist(member, product);
            wishlistRepository.save(newWish);
            return true;
        }
    }
}
