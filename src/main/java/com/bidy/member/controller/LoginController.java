package com.bidy.member.controller;

import com.bidy.member.domain.Member;
import com.bidy.member.dto.LoginDto;
import com.bidy.member.service.MemberService;
import com.bidy.session.SessionConst;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LoginController {

    private final MemberService memberService;

    @Autowired
    public LoginController(MemberService memberService) {
        this.memberService = memberService;
    }

    //일단 처음 시작을 로그인으로 설정함
//    @GetMapping("/")
//    public String home() {
//        return "redirect:/login";
//    }

    //로그이니 페이지
    @GetMapping("/login")
    public String loginForm(Model model){
        //loginDto 객체를 Model에 넣어줘야 th:object="${loginDto}" 가 인식 됨
        model.addAttribute("loginDto", new LoginDto());

        return "login";
    }

    //로그인 처리
    @PostMapping("/login")
    public String login(@Valid LoginDto loginDto,
                        BindingResult bindingResult,
                        HttpSession session,
                        Model model){

        if(bindingResult.hasErrors()){
            return "login";
        }

        try{
            Member member = memberService.login(loginDto);
            session.setAttribute(SessionConst.LOGIN_MEMBER, member); //세션에 담음->로그인!!
            return "redirect:/";  // ***로그인 성공시 홈으로***
        } catch(IllegalArgumentException e){
            if (e.getMessage().contains("이메일")) {
                bindingResult.rejectValue("memberEmail", "none", e.getMessage());
            }
            else if (e.getMessage().contains("비밀번호")) {
                bindingResult.rejectValue("memberPw", "not", e.getMessage());
            }
            return "login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        if (session != null) {
            session.invalidate();
        }
        return "redirect:/";
    }
}
