package com.bidy.member.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class EmailService {
    private final JavaMailSender mailSender;
    //여러명 동시에 인증 요청 보낼 수 있음
    private final Map<String, String> codeStorage = new ConcurrentHashMap<>();
    //인증 만료시간 저장
    private final Map<String, LocalDateTime> expireAt = new ConcurrentHashMap<>();


    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    //이메일 인증 번호 전송
    public void sendCode(String email) {

        //코드 랜덤 생성, 0 ~999999 정수만 6자리
        String code = String.format("%06d", new Random().nextInt(1000000));
        codeStorage.put(email, code);  //생성한 코드 저장
        expireAt.put(email, LocalDateTime.now().plusMinutes(10)); //인증 만료시간= 현재시간 + 10분

        String subject = "[BIDY] 이메일 인증번호";
        String body = "인증번호는 [ " + code + " ] 입니다. 10분 내에 입력해주세요.";

        //메일전송
        if (mailSender != null) {
            SimpleMailMessage msg = new SimpleMailMessage();
            msg.setTo(email);
            msg.setSubject(subject);
            msg.setText(body);
            mailSender.send(msg);
        }

        // 로컬용
        System.out.println("----------------------------------");
        System.out.println("이메일 전송!!!" + email);
        System.out.println("코드 : " + code);
        System.out.println("----------------------------------");
    }
    
    //인증코드 일치 여부
    public boolean verify(String email, String inputCode) {

        String realCode = codeStorage.get(email);

        if (realCode == null) return false;
        //시간 초과시
        if (LocalDateTime.now().isAfter(expireAt.get(email))) return false;
        boolean ok = realCode.equals(inputCode);
        if (ok) {
            // 사용 후 폐기
            codeStorage.remove(email);
            expireAt.remove(email);
        }
        return ok;
    }
}
