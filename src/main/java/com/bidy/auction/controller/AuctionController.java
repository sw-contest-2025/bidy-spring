package com.bidy.auction.controller;

import com.bidy.auction.domain.Bid;
import com.bidy.auction.dto.BidRequestDto;
import com.bidy.auction.service.AuctionService;
import com.bidy.member.domain.Member;
import com.bidy.post.domain.Product;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
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
            Member loginMember = (Member) session.getAttribute("loginMember");
            if (loginMember != null) {
                model.addAttribute("loginId", loginMember.getMemberId());
            }
            // 1. 홈페이지에서 productId를 넘겨 받아 해당 상품을 조회
            Product product = auctionService.findProductById(productId);
            // 2. 최신 입찰 기록(가장 높은 입찰 가격) 5개 조회
            List<Bid> recentBids = auctionService.getRecentBids(product);
            LocalDateTime calculatedTime = product.calculateEndTime();

            int maxBidPrice = recentBids.isEmpty()
                    ? product.getCurrentPrice()
                    : recentBids.stream()
                    .mapToInt(Bid::getBidPrice)
                    .max()
                    .orElse(product.getCurrentPrice());

            String formattedEndTime = "";
            if (calculatedTime != null) {
                ZonedDateTime endTimeKst = calculatedTime.atZone(KST_ZONE_ID);
                formattedEndTime = endTimeKst.format(JS_DATE_FORMATTER);
            }

            System.out.println("DEBUG: Formatted End Time (JS Target): " + formattedEndTime);
            System.out.println("DEBUG: Product Current Price: " + product.getCurrentPrice());

            // 3. 데이터를 JSP로 전달
            model.addAttribute("product", product);
            model.addAttribute("recentBids", recentBids);
            model.addAttribute("endTime", formattedEndTime);
            model.addAttribute("maxBidPrice", maxBidPrice);

            return "auction/auction_detail";

        } catch (NoSuchElementException e){
            return "redirect:/error/404";
        }
    }

    @PostMapping("/bid")
    public String createBid(@ModelAttribute BidRequestDto bidRequestDto, RedirectAttributes rttr, HttpSession session, HttpServletResponse response) throws IOException {
        // 로그인 여부 확인 및 리다이렉트
        Member loginMember = (Member) session.getAttribute("loginMember");

        if (loginMember == null) {
            response.setContentType("text/html; charset=UTF-8");

            String script = "<script>"
                    + "alert('로그인이 필요합니다.');"
                    + "window.location.href='/login';" // alert 후 로그인 페이지로 이동
                    + "</script>";

            response.getWriter().print(script);
            return null;
        }

        bidRequestDto.setBidderId(loginMember.getMemberId());
        try {
            // 1. Service 호출: 입찰 기록 저장 및 가격 갱신
            String successMessage = auctionService.createBid(bidRequestDto);

            rttr.addFlashAttribute("message", successMessage);
        }
        catch (IllegalStateException e){
            rttr.addFlashAttribute("error", e.getMessage());
        }
        catch (NoSuchElementException e) {
            // Service에서 상품을 못 찾았거나, 간혹 DB 회원 정보가 유효하지 않은 경우
            if (e.getMessage() != null && e.getMessage().contains("존재하지 않는 상품")) {
                return "redirect:/error/404";
            }
            // 그 외 NoSuchElementException (Service에서 Member를 못 찾은 경우)
            rttr.addFlashAttribute("error", "회원 정보가 유효하지 않습니다. 다시 로그인해주세요.");
            return "redirect:/login";
        }
        catch (Exception e) {
            rttr.addFlashAttribute("error", "입찰 처리 중 오류가 발생했습니다.");
        }
        return "redirect:/auction/auction_detail?productId=" + bidRequestDto.getProductId();
    }

    @GetMapping("/api/auction/current-price")
    @ResponseBody
    public int getCurrentPrice(@RequestParam("productId") int productId) {
        System.out.println("DEBUG: AJAX Price Request for ID: " + productId);
        Product product = auctionService.findProductById(productId);
        return product.getCurrentPrice();
    }
}
