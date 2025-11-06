package com.bidy.member.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;

@Entity
@Getter @Setter
@NoArgsConstructor //생성자 추가
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id") //매핑 및 제약조건 설정
    private Long memberId;

    @Column(name = "member_email", nullable = false, unique = true, length = 320)
    private String memberEmail;   // 로그인 ID(이메일)

    @Column(name = "member_pw", nullable = false, length = 255)
    private String memberPw;

    @Column(name = "member_name", nullable = false, length = 20)
    private String memberName;

    @Column(name = "member_nickname", nullable = false, unique = true, length = 10)
    private String memberNickname;

    // 요청 스키마가 DATE이므로 LocalDate 사용
    @CreationTimestamp
    @Column(name = "member_create", nullable = false, updatable = false)
    private LocalDate memberCreate;  //생성일자

    @Column(name = "member_role", nullable = false, length = 20)
    private String memberRole = "MEMBER"; // MEMBER, ADMIN (문자열로 저장)

    @Column(name = "member_birthday", nullable = false)
    private LocalDate memberBirthday;
}