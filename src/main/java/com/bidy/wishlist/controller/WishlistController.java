package com.bidy.wishlist.controller;

import com.bidy.wishlist.service.WishlistService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    @PostMapping("/toggle")
    public ResponseEntity<Map<String, Object>> toggleWishlist(
            @RequestParam("memberId") Long memberId,
            @RequestParam("productId") int productId
    ){
        Map<String, Object> response = new HashMap<>();

        try{
            boolean isAdded = wishlistService.toggleWishlist(memberId, productId);
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
