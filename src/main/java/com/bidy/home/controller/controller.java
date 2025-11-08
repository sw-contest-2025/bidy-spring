package com.bidy.home.controller;

import com.bidy.post.domain.Product;
import com.bidy.post.repository.PostProductRepository;
import org.antlr.v4.runtime.tree.pattern.ParseTreePattern;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
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
    public String home(Model model) {
        List<Product> products = postProductRepository.findAll();
        model.addAttribute("products", products);
        System.out.println("상품 수: " + products.size()); // 콘솔 확인
        model.addAttribute("sales", products); // HTML에서 th:each="s : ${sales}" 사용 가능
        return "home"; // home.html
    }
}
