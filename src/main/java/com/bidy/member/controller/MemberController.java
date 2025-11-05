package com.bidy.member.controller;

import com.bidy.member.dto.MemberSignupDto;
import com.bidy.member.service.MemberService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class MemberController {

    private final MemberService memberService;

    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    //회원가입 폼 페이지
    @GetMapping("/signup")
    public String signupForm(Model model){
        model.addAttribute("memberSignupDto", new MemberSignupDto());
        return "signup"; // signup.html 이동
    }

    //회원가입 처리
    @PostMapping("/signup")
    public String signup(@Valid MemberSignupDto memberSignupDto, //@Valid는
                         BindingResult bindingResult,
                         Model model){

        //요효성 검사 실패한 경우
        if(bindingResult.hasErrors()){
            return "signup"; //다시 띄움. 필드별 오류는 html에서 th:errors
        }

        try{
            memberService.signup(memberSignupDto);
        } catch(IllegalArgumentException e){
            //중복 등 실패
            if (e.getMessage().contains("이메일")) {
                bindingResult.rejectValue("memberEmail", "duplicate", e.getMessage());
            }
            else if (e.getMessage().contains("닉네임")) {
                bindingResult.rejectValue("memberNickname", "duplicate", e.getMessage());
            }
            else if (e.getMessage().contains("비밀번호")) {
                bindingResult.rejectValue("memberPwConfirm", "mismatch", e.getMessage());
            }
            return "signup";
        }

        //성공!
        return "redirect:/signup/success";
    }

    //회원가입 성공 페이지
    @GetMapping("/signup/success")
    public String signupSucess(){
        return "signup-success";
    }
}


