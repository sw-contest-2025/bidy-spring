package com.bidy.member.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
public class MemberSignupDto {

    //유효성 검증

    @NotBlank(message = "이메일은 필수 입력 항목입니다.")
    @Email(message = "올바른 이메일 형식이 아닙니다.")
    private String memberEmail;

    @NotBlank(message = "비밀번호는 필수 입력 항목입니다.")
    @Size(min = 8, message = "비밀번호는 8자 이상이어야 합니다")
    @Pattern(
            regexp = "^(?=.{8,})((?=.*[A-Za-z])(?=.*\\d)|(?=.*[A-Za-z])(?=.*[^A-Za-z\\d])|(?=.*\\d)(?=.*[^A-Za-z\\d])).*$",
            message = "비밀번호는 영문,숫자,특수문자 중 2가지 이상을 포함해야 합니다."
    )
    private String memberPw;

    @NotBlank(message = "비밀번호 확인은 필수 입력 항목입니다.")
    private String memberPwConfirm; // 비밀번호 재입력 필드

    @NotBlank(message = "이름은 필수 입력 항목입니다.")
    @Size(max = 20, message = "이름은 20자 이하여야 합니다")
    private String memberName;

    @NotBlank(message = "닉네임은 필수 입력 항목입니다.")
    @Size(max = 10, message = "닉네임은 10자 이하여야 합니다")
    private String memberNickname;

    @NotNull(message = "생년월일은 필수 입력 항목입니다.")
    @DateTimeFormat(pattern = "yyyy-MM-dd") //LocalDate 변환
    private LocalDate memberBirthday;
}
