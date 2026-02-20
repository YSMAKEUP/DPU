package com.dpu.User.dto;

import com.dpu.User.domain.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//회원가입 DTO.
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SignUpRequestDto {
    @NotBlank(message = "이름을 입력해주세요.")
    private String name;

    @NotBlank(message = "이메일을 입력해주세요.")
    @Email(message = "이메일 형식이 아닙니다.")
    private String email;

    @NotBlank(message ="비밀번호를 입력해주세요.")
    private String password;

    private Role userType ;


}


