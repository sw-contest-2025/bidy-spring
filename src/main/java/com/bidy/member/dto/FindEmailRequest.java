package com.bidy.member.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter@Setter
public class FindEmailRequest {
    @NotBlank(message = "이름을 입력해주세요")
    private String memberName;

    @NotNull(message = "생년월일을 입력해주세요")
    @DateTimeFormat(pattern = "yyyy-MM-dd") //LocalDate 변환
    private LocalDate memberBirthday;
}
