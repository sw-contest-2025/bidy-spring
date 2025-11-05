package com.bidy.member.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginDto {

    @NotBlank(message = "이메일(ID)를 입력하세요")
    @Email(message = "올바른 이메일 형식이 아닙니다")
    private String memberEmail;

    @NotBlank(message = "비밀번호를 입혁하세요")
    private String memberPw;
}
