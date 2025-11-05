package com.bidy.member.controller;

import com.bidy.member.domain.Member;
import com.bidy.member.repository.MemberRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MyPageController {

    private final MemberRepository memberRepository;

    public MyPageController(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    //마이페이지
    @GetMapping("/mypage")
    public String myPage(HttpSession session, Model model){

        Member loginMember = (Member) session.getAttribute("member");

        if(loginMember == null) return "redirect:login";

        return "mypage";
    }
}
