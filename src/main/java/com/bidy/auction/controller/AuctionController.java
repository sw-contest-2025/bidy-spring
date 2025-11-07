package com.bidy.auction.controller;

import com.bidy.auction.domain.Bid;
import com.bidy.auction.dto.BidRequestDto;
import com.bidy.auction.service.AuctionService;
import com.bidy.post.domain.Product;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.NoSuchElementException;

@Controller
class AuctionController {
    // AuctionService 주입
    private final AuctionService auctionService;

    public AuctionController(AuctionService auctionService) {
        this.auctionService = auctionService;
    }

    @GetMapping("/auction/auction_detail")
    public String readDetail(@RequestParam("productId") int productId, Model model) {
        try{
            // 1. 홈페이지에서 productId를 넘겨 받아 해당 상품을 조회
            Product product = auctionService.findProductById(productId);
            if (product == null) {
                throw new NoSuchElementException("Product not found");
            }

            // 2. 최신 입찰 기록(가장 높은 입찰 가격) 5개 조회
            List<Bid> recentBids = auctionService.getRecentBids(product);

            // 3. 데이터를 JSP로 전달
            model.addAttribute("product", product);
            model.addAttribute("recentBids", recentBids);
            model.addAttribute("endTime", product.calculateEndTime().toString());

            return "auction/auction_detail";

        } catch (NoSuchElementException e){
            return "redirect:/error/404";
        }
    }

    @PostMapping("/auction/bid")
    public String createBid(@ModelAttribute BidRequestDto bidRequestDto, RedirectAttributes rttr) {
        try {
            // 1. Service 호출: 입찰 기록 저장 및 가격 갱신
            auctionService.createBid(bidRequestDto);

            rttr.addFlashAttribute("message", "입찰이 성공적으로 완료되었습니다.");
        }
        catch (IllegalStateException e){
            rttr.addFlashAttribute("error", e.getMessage());
        } catch (Exception e) {
            rttr.addFlashAttribute("error", "입찰 처리 중 오류가 발생했습니다.");
        }
        return "redirect:/auction/auction_detail";
    }

}
