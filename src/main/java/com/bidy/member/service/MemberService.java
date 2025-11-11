package com.bidy.member.service;

import com.bidy.member.domain.Member;
import com.bidy.member.dto.FindEmailRequest;
import com.bidy.member.dto.LoginDto;
import com.bidy.member.dto.MemberSignupDto;
import com.bidy.member.dto.MemberUpdateDto;
import com.bidy.member.repository.MemberRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private static final String TEMP_PASSWORD_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*";
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    public MemberService(MemberRepository memberRepository,
                         PasswordEncoder passwordEncoder,
                         EmailService emailService){
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    //회원가입
    public Member signup(MemberSignupDto signupDto){

        //이메일 중복확인
        if(memberRepository.existsByMemberEmail(signupDto.getMemberEmail())){
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }

        //닉네임 중복 확인
        if(memberRepository.existsByMemberNickname(signupDto.getMemberNickname())){
            throw new IllegalArgumentException("이미 존재하는 닉네임입니다.");
        }

        //비밀번호 확인 검사
        if(!signupDto.getMemberPw().equals(signupDto.getMemberPwConfirm())){
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다");
        }

        //Dto -> Entity 변환
        Member member = new Member();
        member.setMemberEmail(signupDto.getMemberEmail());
        member.setMemberPw(passwordEncoder.encode(signupDto.getMemberPw())); //비밀번호 암호화
        member.setMemberName(signupDto.getMemberName());
        member.setMemberNickname(signupDto.getMemberNickname());
        member.setMemberBirthday(signupDto.getMemberBirthday());
        member.setMemberRole("MEMBER");

        return memberRepository.save(member);
    }

    public void resetPassword(String email) {
        Member member = memberRepository.findByMemberEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 이메일입니다."));

        String tempPassword = generateTempPassword();
        member.setMemberPw(passwordEncoder.encode(tempPassword));
        emailService.sendTemporaryPassword(email, tempPassword);
    }

    private String generateTempPassword() {
        int length = 12;
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int index = SECURE_RANDOM.nextInt(TEMP_PASSWORD_CHARS.length());
            sb.append(TEMP_PASSWORD_CHARS.charAt(index));
        }
        return sb.toString();
    }

    //로그인
    public Member login(LoginDto loginDto){

        //이메일로 회원 갖고옴
        Optional<Member>memberOpt = memberRepository.findByMemberEmail(loginDto.getMemberEmail());

        //없는 이메일
        if(memberOpt.isEmpty()){
            throw new IllegalArgumentException("가입되지 않은 이메일입니다");
        }

        //비밀번호 일치 여부
        Member member = memberOpt.get();
        if(!passwordEncoder.matches(loginDto.getMemberPw(), member.getMemberPw())){
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다");
        }

        return member;
    }

    //이메일 찾기
    public List<String> findEmails(FindEmailRequest req) {
        List<Member> members = memberRepository.findByMemberNameAndMemberBirthday(
                req.getMemberName(),
                req.getMemberBirthday()
        );

        // 이메일 리스트
        //이메일 아이디 앞 4자리만 보이게 !!!!
        return members.stream()
                .map(Member::getMemberEmail)
                .map(email -> {
                    int atIndex = email.indexOf("@");
                    if (atIndex > 3) {
                        return email.substring(0, 3) + "****" + email.substring(atIndex);
                    } else {
                        return email.charAt(0) + "***" + email.substring(atIndex);
                    }
                })
                .toList();
    }

    //계정 정보 수정
    public Member updateMember(Long memberId, MemberUpdateDto updateDto) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원 정보를 찾을 수 없습니다"));

        //닉네임만 변경할떈 비밀번호 확인 x
        //변경 닉네임 중복 확인. 닉네임이 변경되었고 닉네임이 존재하는가?
        if (!member.getMemberNickname().equals(updateDto.getMemberNickname())){
            if(memberRepository.existsByMemberNickname(updateDto.getMemberNickname())){
                throw new IllegalArgumentException("이미 사용중인 닉네임입니다");
            }
            //닉네임 업데이트
            member.setMemberNickname(updateDto.getMemberNickname());
        }

        //비밀번호 변경
        //비밀 번호 변경은 선택이기 때문에 여기시ㅓ null인지 아닌지 체크해야함..
        //새 비밀번호가 입력되었다면
        if (updateDto.getNewPassword() != null && !updateDto.getNewPassword().isEmpty()) {

            //현재 비밀번호 확인 먼저
            if (!passwordEncoder.matches(updateDto.getCurrentPassword(), member.getMemberPw())) {
                throw new IllegalArgumentException("현재 비밀번호가 일치하지 않습니다");
            }
            //새 비밀번호와 비밀번호 확인 같은지
            if (!updateDto.getNewPassword().equals(updateDto.getNewPasswordConfirm())) {
                throw new IllegalArgumentException("새 비밀번호가 일치하지 않습니다");
            }
            //비밀번호 업데이트
            member.setMemberPw(passwordEncoder.encode(updateDto.getNewPassword()));
        }

        //프로필 이미지 업데이트
        member.setMemberNickname(updateDto.getMemberNickname());

        return member;
    }
}
