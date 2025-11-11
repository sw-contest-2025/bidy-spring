package com.bidy.wishlist.controller;

import com.bidy.member.domain.Member;
import com.bidy.session.SessionConst;
import com.bidy.wishlist.service.WishlistService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/wishlist")
public class WishlistController {
    private final WishlistService wishlistService;
    public WishlistController(WishlistService wishlistService) {
        this.wishlistService = wishlistService;
    }

    // post 요청으로 위시리스트 상태 변경
    @PostMapping("/toggle/{productId}")
    public ResponseEntity<Map<String, Object>> toggleWishlist(
            @PathVariable int productId,
            @ModelAttribute("loginMember") Member loginMember
    ) {
        Map<String, Object> response = new HashMap<>();

        if (loginMember == null) {
            response.put("success", false);
            response.put("message", "로그인 필요");
            return ResponseEntity.status(401).body(response);
        }

        try {
            String action = wishlistService.toggleWishlist(loginMember, productId);
            response.put("success", true);
            response.put("action", action);
            if ("added".equals(action)) {
                response.put("message", "위시리스트에 추가되었습니다.");
            } else {
                response.put("message", "위시리스트에서 삭제되었습니다.");
            }

            return ResponseEntity.ok(response);
        }
        catch(NoSuchElementException e){
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(404).body(response);
        }
        catch(Exception e){
            response.put("success", false);
            response.put("message", "처리 중 오류가 발생했습니다.");
            return ResponseEntity.status(500).body(response);
        }
    }
    @GetMapping("/check/{productId}")
    public ResponseEntity<Map<String, Object>> checkWishlist(
            @PathVariable int productId,
            @ModelAttribute("loginMember") Member loginMember
    ) {
        Map<String, Object> response = new HashMap<>();
        if (loginMember == null) {
            response.put("isWishlisted", false);
            return ResponseEntity.ok(response);
        }

        boolean isWishlisted = wishlistService.getWishlistStatus(loginMember.getMemberId(), productId);
        response.put("isWishlisted", isWishlisted);
        return ResponseEntity.ok(response);
    }
}
