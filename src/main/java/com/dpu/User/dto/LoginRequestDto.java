package com.dpu.User.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class LoginRequestDto {

    private String email;
    private String password; // 비밀번호이기 때문에 jwt를 사용할 예정




}
