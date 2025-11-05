package com.bidy.member.repository;

import com.bidy.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    //이메일로 회원 찾기
    Optional<Member> findByMemberEmail(String memberEmail);

    //닉네임으로 회원 찾기
    Optional<Member> findByMemberNickname(String memberNickname);

    //이메일 중복 확인
    boolean existsByMemberEmail(String memberEmail);

    //닉네임 중복 확인
    boolean existsByMemberNickname(String memberNickname);
}
