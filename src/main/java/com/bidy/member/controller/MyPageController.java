package com.bidy.member.controller;

import com.bidy.auction.repository.BidRepository;
import com.bidy.member.domain.Member;
import com.bidy.member.dto.MemberUpdateDto;
import com.bidy.member.repository.MemberRepository;
import com.bidy.member.service.MemberService;
import com.bidy.post.domain.Product;
import com.bidy.post.repository.PostProductRepository;
import com.bidy.session.SessionConst;
import com.bidy.wishlist.domain.Wishlist;
import com.bidy.wishlist.repository.WishlistRepository;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import com.bidy.auction.domain.Bid;

import java.util.ArrayList;
import java.util.List;

@Controller
public class MyPageController {

    private final MemberRepository memberRepository;
    private final MemberService memberService;
    private final PostProductRepository postProductRepository;
    private final BidRepository bidRepository;
    private final WishlistRepository wishlistRepository;

    public MyPageController(MemberRepository memberRepository,
                            MemberService memberService,
                            PostProductRepository postProductRepository,
                            BidRepository bidRepository,
                            WishlistRepository wishlistRepository) {

        this.memberRepository = memberRepository;
        this.memberService = memberService;
        this.postProductRepository = postProductRepository;
        this.bidRepository = bidRepository;
        this.wishlistRepository = wishlistRepository;
    }

    //마이페이지
    @GetMapping("/mypage")
    public String myPage(HttpSession session, Model model){

        //세션에서 로그인 된 멤버 꺼냄
        Member loginMember = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);

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
        int tradeCount = 3;  // TODO: <--- 계산해야함
        List<Product> salesList = postProductRepository.findByUser_MemberId(loginMember.getMemberId());

        //꺼낸거 보냄
        model.addAttribute("member", member);
        model.addAttribute("tradeCount", tradeCount);
        model.addAttribute("sales", salesList);
        model.addAttribute("activeTab", "sales"); //현재 탭 어딘지 알려주는 용(View)

        return "mypage";
    }

    //구매 내역
    @GetMapping("/mypage/purchases")
    public String myPagePurchases(HttpSession session, Model model){

        //세션에서 로그인 된 멤버 꺼냄
        Member loginMember = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);

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
        List<Product> purchasesList = postProductRepository.findByWinner_MemberId(loginMember.getMemberId());

        //꺼낸거 보냄
        model.addAttribute("member", member);
        model.addAttribute("tradeCount", tradeCount);
        model.addAttribute("purchases", purchasesList);
        model.addAttribute("activeTab", "purchases"); //현재 탭 어딘지 알려주는 용(View)

        return "mypage-purchases";
    }

    //입찰 내역
    @GetMapping("/mypage/bid")
    public String myPageBid(HttpSession session, Model model){

        //세션에서 로그인 된 멤버 꺼냄
        Member loginMember = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);

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
        List<Bid> bidList = bidRepository.findByBidder_MemberId(loginMember.getMemberId());

        //꺼낸거 보냄
        model.addAttribute("member", member);
        model.addAttribute("tradeCount", tradeCount);
        model.addAttribute("bid", bidList);
        model.addAttribute("activeTab", "bid"); //현재 탭 어딘지 알려주는 용(View)

        return "mypage-bid";
    }


    @GetMapping("/mypage/wishlist")
    public String myPageWishList(HttpSession session, Model model){

        //세션에서 로그인 된 멤버 꺼냄
        Member loginMember = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);

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
        List<Wishlist> wishList = wishlistRepository.findByMember_MemberId(loginMember.getMemberId());
        List<Product> wishlistProducts = new ArrayList<>();
        for (Wishlist wishlist : wishList) {
            wishlistProducts.add(wishlist.getProduct());
        }

        //꺼낸거 보냄
        model.addAttribute("member", member);
        model.addAttribute("tradeCount", tradeCount);
        model.addAttribute("wish", wishList);
        model.addAttribute("wishlistProducts", wishlistProducts);
        model.addAttribute("activeTab", "wishlist"); //현재 탭 어딘지 알려주는 용(View)

        return "mypage-wishlist";
    }

    //계정 수정
    @GetMapping("/mypage/edit")
    public String myPageEdit(HttpSession session, Model model){

        //세션에서 로그인 된 멤버 꺼냄
        Member loginMember = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);

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
    
        //현재 정보를 미리 폼에 담아둠
        MemberUpdateDto updateDto = new MemberUpdateDto();
        updateDto.setMemberNickname(member.getMemberNickname());
        updateDto.setProfileImageUrl(member.getProfileImageUrl());
        
        //거래횟수 (판매 횟수+구매횟수)
        int tradeCount = 3;  // <--- 계산해야함

        model.addAttribute("member", member);
        model.addAttribute("updateDto", updateDto);
        model.addAttribute("tradeCount", tradeCount);
        model.addAttribute("activeTab", "edit"); //현재 탭 어딘지 알려주는 용(View)

        return "mypage-edit";
    }

    //회원정보 수정 처리
    @PostMapping("/mypage/edit")
    public String updateMember(@Valid @ModelAttribute("updateDto") MemberUpdateDto updateDto,
                               BindingResult bindingResult,
                               HttpSession session,
                               Model model) {

        Member loginMember = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);
        if(loginMember == null) return "redirect:/";

        //유효성 검사 실패
        if (bindingResult.hasErrors()) {
            // 레이아웃에 필요한 정보 다시 모델에 추가
            Member member = memberRepository.findById(loginMember.getMemberId()).get(); // 이미 로그인된 회원이므로 Optional.get() 사용 가능
            model.addAttribute("member", member);
            model.addAttribute("tradeCount", 3);
            model.addAttribute("activeTab", "edit");
            return "mypage-edit";
        }

        try {
            //정보 업데이트
            Member updatedMember = memberService.updateMember(loginMember.getMemberId(), updateDto);

            //세션 정보 업데이트
            session.setAttribute(SessionConst.LOGIN_MEMBER, updatedMember);

            //성공 시 GET으로 리다이렉트 (success 쿼리 파라미터로 성공 메시지 표시)
            return "redirect:/mypage/edit?success";

        } catch (IllegalArgumentException e) {
            //비즈니스 로직 오류 처리 (현재 비밀번호 불일치, 닉네임 중복, 새 비밀번호 불일치)
            if (e.getMessage().contains("현재 비밀번호")) {
                bindingResult.rejectValue("currentPassword", "mismatch", e.getMessage());
            } else if (e.getMessage().contains("닉네임")) {
                bindingResult.rejectValue("memberNickname", "duplicate", e.getMessage());
            } else if (e.getMessage().contains("새 비밀번호")) {
                bindingResult.rejectValue("newPasswordConfirm", "mismatch", e.getMessage());
            }

            // 오류 발생 시 레이아웃 정보 다시 추가
            Member member = memberRepository.findById(loginMember.getMemberId()).get();
            model.addAttribute("member", member);
            model.addAttribute("tradeCount", 3); ///<----------
            model.addAttribute("activeTab", "edit");
            return "mypage-edit"; // 폼 다시 띄움
        }
    }
}
