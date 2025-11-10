package com.bidy.wishlist.controller;

import com.bidy.member.domain.Member;
import com.bidy.session.SessionConst;
import com.bidy.wishlist.service.WishlistService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

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
            HttpSession session
    ) {
        Map<String, Object> response = new HashMap<>();
        Member loginMember = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER); //세션으로 현재 사용자 아이디를 가져옴

        if (loginMember == null) {
            response.put("success", false);
            response.put("message", "로그인 필요");
            return ResponseEntity.status(401).body(response);
        }

        try {
            boolean isAdded = wishlistService.toggleWishlist(loginMember.getMemberId(), productId);
            response.put("success", true);
            response.put("action", isAdded ? "added" : "removed");
            response.put("message", isAdded ? "위시리스트에 추가되었습니다." : "위시리스트에서 삭제되었습니다.");
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
}
