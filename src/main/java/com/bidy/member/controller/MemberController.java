package com.bidy.member.controller;

import com.bidy.member.domain.Member;
import com.bidy.member.dto.MemberSignupDto;
import com.bidy.member.service.EmailService;
import com.bidy.member.service.MemberService;
import com.bidy.session.SessionConst;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MemberController {

    private static final String VERIFIED_EMAIL = "VERIFIED_EMAIL"; // 세션 키

    private final MemberService memberService;
    private final EmailService emailService;

    @Autowired
    public MemberController(MemberService memberService, EmailService emailService) {
        this.memberService = memberService;
        this.emailService = emailService;
    }

    //회원가입 폼 페이지
    @GetMapping("/signup")
    public String signupForm(Model model,
                             @ModelAttribute("memberSignupDto") MemberSignupDto dto,  //폼에서 입력하던 값
                             @RequestParam(value="codeSent", required=false) Boolean codeSent, //이메일 인증 버튼을 눌렀는가??
                             @RequestParam(value="verified", required=false) Boolean verified){ //이메일 인증 성공?

        //처음 들어왔을때! 객체생성
        if (!model.containsAttribute("memberSignupDto")) {
            model.addAttribute("memberSignupDto", new MemberSignupDto());
        }

        //required=false 라서 없으면 그냥 null로 들어옵
        model.addAttribute("codeSent", codeSent != null && codeSent);
        model.addAttribute("emailVerified", verified != null && verified);

        return "signup";
    }

    //회원가입 처리
    @PostMapping("/signup")
    public String signup(@Valid MemberSignupDto memberSignupDto, //@Valid는
                         BindingResult bindingResult,
                         Model model,
                         HttpServletRequest request){

        //요효성 검사 실패한 경우
        if(bindingResult.hasErrors()){
            return "signup"; //다시 띄움. 필드별 오류는 html에서 th:errors
        }

        //이메일 인증 안됐을때
        HttpSession session = request.getSession(false); //세션 없으면 null반환
        String verifiedEmail = (session != null) ? (String)session.getAttribute("VERIFIED_EMAIL") : null;
        if(verifiedEmail == null || !verifiedEmail.equals(memberSignupDto.getMemberEmail())){
            model.addAttribute("errorMessage", "이메일 인증을 완료해주세요");
            return "signup";
        }

        try{
            Member saved = memberService.signup(memberSignupDto);

            //기존 세션 무효화
            //새션 로테이션! 보안관련, 로그인 할때 로그인 전 세션 그대로 쓰면 안됨!!
            if (session != null) session.invalidate();
            HttpSession newSession = request.getSession(true);

            //로그인 유지
            newSession.setAttribute(SessionConst.LOGIN_MEMBER, saved);

            //성공!
            return "redirect:/signup/success";

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
    }

    //회원가입 성공 페이지
    @GetMapping("/signup/success")
    public String signupSucess(HttpSession session, Model model){
        Member loginMember = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);
        if (loginMember != null) {
            model.addAttribute("memberNickname", loginMember.getMemberNickname());
        }
        return "signup-success";
    }
}


