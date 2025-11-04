package com.bidy.auction.controller;

import com.bidy.auction.domain.Bid;
import com.bidy.auction.service.AuctionService;
import com.bidy.home.domain.Product;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.NoSuchElementException;

@Controller
class AuctionController {
    // AuctionService 주입
    private final AuctionService auctionService;

    public AuctionController(AuctionService auctionService) {
        this.auctionService = auctionService;
    }

    @GetMapping("/bidy/auction_detail")
    public String readDetail(@RequestParam("productId") int productId, Model model) {
        try{
            // 1. 홈페이지에서 productId를 넘겨 받아 해당 상품을 조회
            Product product = auctionService.findProductById(productId);

            // 2. 최신 입찰 기록(가장 높은 입찰 가격) 5개 조회
            List<Bid> recentBids = auctionService.getRecentBids(product);

            // 3. 데이터를 JSP로 전달
            model.addAttribute("product", product);
            model.addAttribute("recentBids", recentBids);

            return "bidy/auction_detail";

        } catch (NoSuchElementException e){
            return "redirect:/error/404";
        }
    }

}
