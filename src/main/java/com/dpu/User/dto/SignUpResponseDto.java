package com.dpu.User.dto;

import com.dpu.User.domain.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SignUpResponseDto {

    private Long userId;
    private String email;
    private String name;
    private Role userType;
    private LocalDateTime createdAt;






}
