package com.bidy.member.controller;

import com.bidy.member.service.EmailService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController//JSON 반환, API전용 컨트롤러, 데이터만 주고받음
@RequestMapping("/email")
public class EmailController {

    private final EmailService emailService;

    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }
    
    //보내기
    @PostMapping("/send")
    public ResponseEntity<?> send(@RequestParam String memberEmail) {
        emailService.sendCode(memberEmail);
        return ResponseEntity.ok(Map.of("message", "인증번호가 전송되었습니다."));
    }
    
    //인증
    @PostMapping("/verify")
    public ResponseEntity<?> verify(@RequestParam String memberEmail, @RequestParam String code) {
        boolean verified = emailService.verify(memberEmail, code);
        return ResponseEntity.ok(Map.of("verified", verified));
    }
}
