package com.bidy.home.controller;

import com.bidy.post.domain.Product;
import com.bidy.post.repository.PostProductRepository;
import com.bidy.session.SessionConst;
import jakarta.servlet.http.HttpSession;
import org.antlr.v4.runtime.tree.pattern.ParseTreePattern;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;

import java.security.Principal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


@Controller
public class controller {
    private final PostProductRepository postProductRepository;

    // 생성자 주입
    public controller(PostProductRepository postProductRepository) {
        this.postProductRepository = postProductRepository;
    }

    //첫 화면으로 설정
    @GetMapping("/") // 홈페이지 루트 경로
    public String home(
            @RequestParam(value = "category", required = false) String category,
            HttpSession session
            , Model model) {
        List<Product> products;

        if (category == null || category.equals("전체")) { // 기본&전체 -> 모두 반환
            products = postProductRepository.findAll();
        } else { // 카테고리 있음 -> 해당 카테고리의 상품 반환
            products = postProductRepository.findByCategory(category);
        }

        model.addAttribute("products", products);
        model.addAttribute("selectedCategory", category);
        System.out.println("상품 수: " + products.size()); // 콘솔 확인
        model.addAttribute("sales", products); // HTML에서 th:each="s : ${sales}" 사용 가능

        // 세션에서 로그인 정보 확인
        Object loginMember = session.getAttribute(SessionConst.LOGIN_MEMBER);
        boolean loggedIn = (loginMember != null); //로그인 여부 확인하기 (true면 마이페이지, false면 로그인회원가입)
        model.addAttribute("loggedIn", loggedIn);

        return "home"; // home.html
    }

    // postName에 따른 검색
    @GetMapping("/search")
    public String search(
            @RequestParam(required = false) String keyword,
            Model model) {
        List<Product> searchResults;

        if (keyword == null || keyword.trim().isEmpty()) { // 검색어 없음 -> 모두 반환
            searchResults = postProductRepository.findAll();
        } else { // 검색어 있음 -> 해당 키워드 들어간 상품 반환
            searchResults = postProductRepository.findByPostNameContainingIgnoreCase(keyword);
        }

        model.addAttribute("products", searchResults);
        model.addAttribute("keyword", keyword);
        return "home";
    }
}
