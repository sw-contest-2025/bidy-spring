package com.bidy.member.service;

import com.bidy.member.domain.Member;
import com.bidy.member.dto.LoginDto;
import com.bidy.member.dto.MemberSignupDto;
import com.bidy.member.repository.MemberRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public MemberService(MemberRepository memberRepository, PasswordEncoder passwordEncoder){
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
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

    //이메일 찾기(이름, 생년월일로)


    //비밀번호 찾기 (이메일로 임시 비밀번호 전송)
}
