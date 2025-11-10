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
    public String home(Model model, HttpSession session) {
        List<Product> products = postProductRepository.findAll();
        model.addAttribute("products", products); //HTML에서 th:each="s : ${products}" 사용 가능 -자바스크립트에 쓰임

        // 세션에서 로그인 정보 확인
        Object loginMember = session.getAttribute(SessionConst.LOGIN_MEMBER);
        boolean loggedIn = (loginMember != null); //로그인 여부 확인하기 (true면 마이페이지, false면 로그인회원가입)
        model.addAttribute("loggedIn", loggedIn);

        return "home"; // home.html
    }
}
