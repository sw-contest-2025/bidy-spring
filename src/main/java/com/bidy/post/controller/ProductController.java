package com.bidy.post.controller;

import com.bidy.post.domain.Product;
import com.bidy.member.domain.Member;
import com.bidy.member.repository.MemberRepository;
import com.bidy.home.repository.ProductRepository;
import com.bidy.post.repository.PostProductRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import java.util.List;


import java.security.Principal;

@Controller
@RequestMapping("/products")
public class ProductController {

    private final ProductRepository productRepository;
    private final PostProductRepository postProductRepository;
    private final MemberRepository memberRepository;

    public ProductController(ProductRepository productRepository, PostProductRepository postProductRepository, MemberRepository memberRepository) {
        this.productRepository = productRepository;
        this.postProductRepository = postProductRepository;
        this.memberRepository = memberRepository;
    }

    @GetMapping // 홈페이지 상품 리스트 페이지 매핑
    public String listProducts(Model model) {
        // DB에서 모든 상품 조회
        List<Product> products = postProductRepository.findAll();
        model.addAttribute("products", products); // 모델에 담기
        return "product-list"; // thymeleaf 파일명(product-list.html)
    }

    // 게시물 작성 폼 보여주기
    @GetMapping("/new")
    public String showPostForm() { //   /products/new 페이지
        return "post"; // post.html
    }

    // 게시물 작성 처리
    public String submitPost(@ModelAttribute Product product, Principal principal,
                             @RequestParam String deliveryMethod) {
        Member user;
        // 로그인 여부 체크
        if (principal != null) { // 로그인한 사용자
            String email = principal.getName();
            user = memberRepository.findByMemberEmail(email)
                    .orElseThrow(() -> new IllegalArgumentException("회원 정보가 없습니다."));
        } else { // 로그인 안 되어있으면 테스트용 회원 사용
            user = memberRepository.findByMemberEmail("5678@mail.com")
                    .orElseThrow(() -> new IllegalArgumentException("테스트 회원이 존재하지 않습니다."));
        }
        product.setUser(user);                  // Product에 작성자 자동 저장
        product.setDeliveryMethod(deliveryMethod); // 배송 방법 저장
        postProductRepository.save(product);    // DB에 저장
        return "redirect:/home";                // 홈으로 이동
    }
}
