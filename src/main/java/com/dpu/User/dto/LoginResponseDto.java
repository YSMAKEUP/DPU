package com.dpu.User.dto;

import com.dpu.User.domain.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseDto {

    private String result;
    private String token;
    private Long userId;
    private Role userType;


}
