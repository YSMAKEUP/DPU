package com.dpu.User.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UserUpdateDto {
    private String name;
    private String email;
    private String newPassword;
    private String confirmPassword;
}