package com.bidy.member.controller;

import com.bidy.member.dto.MemberSignupDto;
import com.bidy.member.service.EmailService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping("/signup/email")
public class EmailWebController {

    //이메일 인증 버튼 누르면 인증 코드 작성칸 뜨게 하는용
    private final EmailService emailService;
    private static final String VERIFIED_EMAIL = "VERIFIED_EMAIL";

    // 인증번호 전송,,인증 버튼누르면 여기로
    @PostMapping("/send")
    public String send(@ModelAttribute MemberSignupDto dto,
                       RedirectAttributes ra) {
        if (dto.getMemberEmail() == null || dto.getMemberEmail().isBlank()) {
            ra.addFlashAttribute("errorMessage", "이메일을 입력해주세요.");
            ra.addFlashAttribute("memberSignupDto", dto); //입력값 유지
            return "redirect:/signup";
        }
        emailService.sendCode(dto.getMemberEmail());
        ra.addAttribute("codeSent", true);   // 전송하고 인증코드 입력칸 띄우기
        ra.addFlashAttribute("memberSignupDto", dto);   // 입력값 유지..플래시
        ra.addFlashAttribute("verifyOk", "인증번호를 해당 이메일로 전송했습니다");
        return "redirect:/signup";
    }

    // 인증번호 확인, 확인 버튼누르면 여기로
    @PostMapping("/verify")
    public String verify(@RequestParam String code,
                         @ModelAttribute MemberSignupDto dto,
                         HttpSession session,
                         RedirectAttributes ra) {

        if (dto.getMemberEmail() == null || dto.getMemberEmail().isBlank()) {
            ra.addAttribute("codeSent", true);
            ra.addFlashAttribute("verifyError", "이메일을 먼저 입력하세요.");
            ra.addFlashAttribute("memberSignupDto", dto);
            return "redirect:/signup";
        }
        boolean ok = emailService.verify(dto.getMemberEmail(), code);
        if (ok) {
            session.setAttribute(VERIFIED_EMAIL, dto.getMemberEmail());
            ra.addAttribute("verified", true);
            ra.addFlashAttribute("verifyOk", "이메일 인증 완료");
        } else {
            ra.addAttribute("codeSent", true); // 실패해도 입력칸 유지!!
            ra.addFlashAttribute("verifyError", "인증 실패");
        }
        ra.addFlashAttribute("memberSignupDto", dto); //값 유지하기
        return "redirect:/signup";
    }
}
