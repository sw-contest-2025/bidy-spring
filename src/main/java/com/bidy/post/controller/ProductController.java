package com.bidy.post.controller;

import com.bidy.post.domain.Product;
import com.bidy.member.domain.Member;
import com.bidy.member.repository.MemberRepository;
import com.bidy.post.repository.PostProductRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import java.util.List;


import java.security.Principal;

@Controller
@RequestMapping("/products")
public class ProductController {

    private final PostProductRepository postProductRepository;
    private final MemberRepository memberRepository;

    public ProductController(PostProductRepository postProductRepository, MemberRepository memberRepository) {
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
    @PostMapping("/post")
    public String submitPost(@ModelAttribute Product product, HttpSession session,
                             @RequestParam String deliveryMethod) {

        Long memberId = (Long) session.getAttribute("memberId");
        if (memberId == null) { // 혹시라도 로그인 안된 경우
            return "redirect:/login";
        }

        Member user = new Member();
        user.setMemberId(memberId);
        product.setUser(user);                  // Product에 작성자 자동 저장
        product.setDeliveryMethod(deliveryMethod); // 배송 방법 저장
        postProductRepository.save(product);    // DB에 저장
        return "redirect:/";                // 홈으로 이동
    }
}
