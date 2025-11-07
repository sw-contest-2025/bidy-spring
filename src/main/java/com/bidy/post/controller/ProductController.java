package com.bidy.post.controller;

import com.bidy.post.domain.Product;
import com.bidy.member.domain.Member;
import com.bidy.member.repository.MemberRepository;
import com.bidy.home.repository.ProductRepository;
import com.bidy.post.repository.PostProductRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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

    // 게시물 작성 폼 보여주기
    @GetMapping("/new")
    public String showPostForm() { //   /products/new 페이지
        return "post"; // post.html
    }

    // 게시물 작성 처리
    @PostMapping("/post") //작성하기 버튼 누르면 이쪽으로 이동
    public String submitPost(@ModelAttribute Product product, Principal principal , @RequestParam String deliveryMethod) {

        // 로그인 여부 체크
        String email;
        if (principal != null)
            email = principal.getName(); // 로그인 사용자
        else
            email = "5678@mail.com"; // 일단은 게스트 계정으로 테스트함

        Member user = memberRepository.findByMemberEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("회원 정보가 없습니다."));

        product.setUser(user);  // Product에 작성자 자동저장


        product.setDeliveryMethod(deliveryMethod);
        // Post 전용 Repository에 저장
        postProductRepository.save(product);

        return "redirect:/home"; //홈으로 돌아감
    }
}
