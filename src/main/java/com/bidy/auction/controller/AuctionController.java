package com.bidy.auction.controller;

import com.bidy.auction.domain.Bid;
import com.bidy.auction.dto.BidRequestDto;
import com.bidy.auction.service.AuctionService;
import com.bidy.member.domain.Member;
import com.bidy.post.domain.Product;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.NoSuchElementException;

@Controller
@RequestMapping("/auction")
class AuctionController {
    // AuctionService 주입
    private final AuctionService auctionService;

    public AuctionController(AuctionService auctionService) {
        this.auctionService = auctionService;
    }

    private static final ZoneId KST_ZONE_ID = ZoneId.of("Asia/Seoul");
    private static final DateTimeFormatter JS_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXXX");

    @GetMapping("/auction_detail")
    public String readDetail(@RequestParam("productId") int productId, Model model, HttpSession session) {
        System.out.println("DEBUG: Incoming Request for Product ID: " + productId);
        try{
            // 로그인한 회원 정보 조회
            Member loginMember = (Member) session.getAttribute("member");
            if (loginMember != null) {
                model.addAttribute("loginId", loginMember.getMemberId());
            }
            // 홈페이지에서 productId를 넘겨 받아 해당 상품을 조회
            Product product = auctionService.getProductDetailAndUpdateViews((long) productId);
            List<Bid> recentBids = auctionService.getRecentBids(product);
            LocalDateTime calculatedTime = product.calculateEndTime();

            // 태그 기반 추천
            List<Product> recommendedProducts = auctionService.getRecommendProducts((long) productId);

            int maxBidPrice = auctionService.getHighestBidPrice(product);

            String formattedEndTime = "";
            if (calculatedTime != null) {
                ZonedDateTime endTimeKst = calculatedTime.atZone(KST_ZONE_ID);
                formattedEndTime = endTimeKst.format(JS_DATE_FORMATTER);
            }

            System.out.println("DEBUG: Formatted End Time (JS Target): " + formattedEndTime);
            System.out.println("DEBUG: Product Current Price: " + product.getCurrentPrice());

            // 데이터를 JSP로 전달
            model.addAttribute("product", product);
            model.addAttribute("recentBids", recentBids);
            model.addAttribute("endTime", formattedEndTime);
            model.addAttribute("maxBidPrice", maxBidPrice);
            model.addAttribute("recommendedProducts", recommendedProducts);

            return "auction/auction_detail";

        } catch (NoSuchElementException e){
            return "redirect:/error/404";
        }
    }

    @PostMapping("/bid")
    public String createBid(
            @ModelAttribute BidRequestDto bidRequestDto,
            @ModelAttribute("loginMember") Member loginMember,
            RedirectAttributes rttr) {
        if (loginMember == null) {
            rttr.addFlashAttribute("message", "로그인이 필요합니다.");
            return "redirect:/login";
        }

        try {
            String successMessage = auctionService.createBid(bidRequestDto, loginMember.getMemberId());

            rttr.addFlashAttribute("message", successMessage);
        }
        catch (IllegalStateException e){
            rttr.addFlashAttribute("error", e.getMessage());
        }
        catch (NoSuchElementException e) {
            if (e.getMessage() != null && e.getMessage().contains("존재하지 않는 상품")) {
                return "redirect:/error/404";
            }
            rttr.addFlashAttribute("error", "회원 정보가 유효하지 않습니다. 다시 로그인해주세요.");
            return "redirect:/login";
        }
        catch (Exception e) {
            rttr.addFlashAttribute("error", "입찰 처리 중 오류가 발생했습니다.");
        }
        return "redirect:/auction/auction_detail?productId=" + bidRequestDto.getProductId();
    }

    @GetMapping("/api/current-price")
    @ResponseBody
    public int getCurrentPrice(@RequestParam("productId") int productId) {
        System.out.println("DEBUG: AJAX Price Request for ID: " + productId);
        Product product = auctionService.findProductById(productId);
        return product.getCurrentPrice();
    }
}
