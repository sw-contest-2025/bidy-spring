package com.bidy.member.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
public class MemberUpdateDto {
    // 닉네임 수정
    @NotBlank(message = "닉네임은 필수 입력 항목입니다.")
    @Size(min = 2, max = 10, message = "닉네임은 2자 이상 10자 이하로 입력해주세요.")
    private String memberNickname;

    // 비밀번호 변경에 사용
    private String currentPassword; // 현재 비밀번호
    private String newPassword;     // 새 비밀번호
    private String newPasswordConfirm; // 새 비밀번호 확인

    // 프로필 이미지 URL 나중에 이미지 추가 기능 넣어야함
    private String profileImageUrl;
}