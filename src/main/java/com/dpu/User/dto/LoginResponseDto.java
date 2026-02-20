package com.dpu.User.dto;

import com.dpu.User.domain.Role;
import lombok.*;


@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponseDto {

    private String result;
    private Long userId;
    private Role userType;


}
