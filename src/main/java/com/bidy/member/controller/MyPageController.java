package com.bidy.member.controller;

import com.bidy.member.domain.Member;
import com.bidy.member.repository.MemberRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class MyPageController {

    private final MemberRepository memberRepository;

    public MyPageController(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    //마이페이지
    @GetMapping("/mypage")
    public String myPage(HttpSession session, Model model){

        //세션에서 로그인 된 멤버 꺼냄
        Member loginMember = (Member) session.getAttribute("member");
        
        //로그인 안되있으면 로그인 페이지로
        if(loginMember == null) return "redirect:/login";

        //DB에서 다시 아이디로 조회해서 새로 갖고옴(수정된 경우를 반영하기위함)
       Member member = memberRepository.findById(loginMember.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("회원 정보를 찾을 수 없습니다."));
        //findById는 <Optional>로 반환돼서 Member로 바로 받지않고
        //.orElseThrow 로 <Optional>에서 Member꺼내고 안에 값이 없으면 예외 던짐

        if (member.getProfileImageUrl() == null) {
            member.setProfileImageUrl("/images/profile_temp.png"); // 임시 이미지
        }


        //거래횟수 (판매 횟수+구매횟수)
        int tradeCount = 3;  // <--- 계산해야함
        //List<Product> salesList = productRepository.findByMemberId(loginMember.getMemberId());

        //테스트용
        List<Map<String, Object>> salesList = new ArrayList<>();
        Map<String, Object> p1 = new HashMap<>();
        p1.put("title", "아이폰 13 미니");
        p1.put("name", "아이폰");
        p1.put("price", 1000000);
        p1.put("status", "판매중");
        p1.put("imageUrl","/images/product1.png");

        Map<String, Object> p2 = new HashMap<>();
        p2.put("title", "닌텐도 스위치 OLED");
        p2.put("name", "닌텐도");
        p2.put("price", 40000);
        p2.put("status", "예약중");
        p2.put("imageUrl", null);

        salesList.add(p1);
        salesList.add(p2);
        //

        //꺼낸거 보냄
        model.addAttribute("member", member);
        model.addAttribute("tradeCount", tradeCount);
        model.addAttribute("sales", salesList);
        model.addAttribute("activeTab", "sales"); //현재 탭 어딘지 알려주는 용(View)

        return "mypage";
    }

    @GetMapping("/mypage/purchases")
    public String myPagePurchases(HttpSession session, Model model){

        //세션에서 로그인 된 멤버 꺼냄
        Member loginMember = (Member) session.getAttribute("member");

        //로그인 안되있으면 홈 페이지로
        if(loginMember == null) return "redirect:/";

        //DB에서 다시 아이디로 조회해서 새로 갖고옴(수정된 경우를 반영하기위함)
        Member member = memberRepository.findById(loginMember.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("회원 정보를 찾을 수 없습니다."));
        //findById는 <Optional>로 반환돼서 Member로 바로 받지않고
        //.orElseThrow 로 <Optional>에서 Member꺼내고 안에 값이 없으면 예외 던짐

        if (member.getProfileImageUrl() == null) {
            member.setProfileImageUrl("/images/profile_temp.png"); // 임시 이미지
        }

        //거래횟수 (판매 횟수+구매횟수)
        int tradeCount = 3;  // <--- 계산해야함
        //List<Product> purchasesList = bidRepository.findByMemberId(loginMember.getMemberId());

        //테스트용
        List<Map<String, Object>> purchasesList = new ArrayList<>();
        Map<String, Object> p1 = new HashMap<>();
        p1.put("title", "아이폰 13 미니");
        p1.put("name", "아이폰");
        p1.put("price", 1000000);
        p1.put("status", "판매중");
        p1.put("imageUrl","/images/product1.png");

        Map<String, Object> p2 = new HashMap<>();
        p2.put("title", "닌텐도 스위치 OLED");
        p2.put("name", "닌텐도");
        p2.put("price", 40000);
        p2.put("status", "예약중");
        p2.put("imageUrl", null);

        purchasesList.add(p1);
        purchasesList.add(p2);
        //

        //꺼낸거 보냄
        model.addAttribute("member", member);
        model.addAttribute("tradeCount", tradeCount);
        model.addAttribute("sales", purchasesList);
        model.addAttribute("activeTab", "purchases"); //현재 탭 어딘지 알려주는 용(View)

        return "mypage-purchases";
    }

    /*
    구현할것
    */
    @GetMapping("/mypage/wishlist")
    public String myPageWishList(HttpSession session, Model model){

        //세션에서 로그인 된 멤버 꺼냄
        Member loginMember = (Member) session.getAttribute("member");

        //로그인 안되있으면 홈 페이지로
        if(loginMember == null) return "redirect:/";

        //DB에서 다시 아이디로 조회해서 새로 갖고옴(수정된 경우를 반영하기위함)
        Member member = memberRepository.findById(loginMember.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("회원 정보를 찾을 수 없습니다."));
        //findById는 <Optional>로 반환돼서 Member로 바로 받지않고
        //.orElseThrow 로 <Optional>에서 Member꺼내고 안에 값이 없으면 예외 던짐

        if (member.getProfileImageUrl() == null) {
            member.setProfileImageUrl("/images/profile_temp.png"); // 임시 이미지
        }

        //거래횟수 (판매 횟수+구매횟수)
        int tradeCount = 3;  // <--- 계산해야함
        //List<Product> purchasesList = bidRepository.findByMemberId(loginMember.getMemberId());

        //테스트용
        List<Map<String, Object>> purchasesList = new ArrayList<>();
        Map<String, Object> p1 = new HashMap<>();
        p1.put("title", "아이폰 13 미니");
        p1.put("name", "아이폰");
        p1.put("price", 1000000);
        p1.put("status", "판매중");
        p1.put("imageUrl","/images/product1.png");

        Map<String, Object> p2 = new HashMap<>();
        p2.put("title", "닌텐도 스위치 OLED");
        p2.put("name", "닌텐도");
        p2.put("price", 40000);
        p2.put("status", "예약중");
        p2.put("imageUrl", null);

        purchasesList.add(p1);
        purchasesList.add(p2);
        //

        //꺼낸거 보냄
        model.addAttribute("member", member);
        model.addAttribute("tradeCount", tradeCount);
        model.addAttribute("sales", purchasesList);
        model.addAttribute("activeTab", "wishlist"); //현재 탭 어딘지 알려주는 용(View)

        return "mypage-wishlist";
    }

    @GetMapping("/mypage/edit")
    public String myPageEdit(HttpSession session, Model model){

        //세션에서 로그인 된 멤버 꺼냄
        Member loginMember = (Member) session.getAttribute("member");

        //로그인 안되있으면 홈 페이지로
        if(loginMember == null) return "redirect:/";

        //DB에서 다시 아이디로 조회해서 새로 갖고옴(수정된 경우를 반영하기위함)
        Member member = memberRepository.findById(loginMember.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("회원 정보를 찾을 수 없습니다."));
        //findById는 <Optional>로 반환돼서 Member로 바로 받지않고
        //.orElseThrow 로 <Optional>에서 Member꺼내고 안에 값이 없으면 예외 던짐

        if (member.getProfileImageUrl() == null) {
            member.setProfileImageUrl("/images/profile_temp.png"); // 임시 이미지
        }

        //거래횟수 (판매 횟수+구매횟수)
        int tradeCount = 3;  // <--- 계산해야함

        model.addAttribute("member", member);
        model.addAttribute("tradeCount", tradeCount);
        model.addAttribute("activeTab", "edit"); //현재 탭 어딘지 알려주는 용(View)

        return "mypage-edit";
    }
}
