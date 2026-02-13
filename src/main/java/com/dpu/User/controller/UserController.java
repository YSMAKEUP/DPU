package com.dpu.User.controller;

import com.dpu.User.dto.LoginRequestDto;
import com.dpu.User.dto.LoginResponseDto;
import com.dpu.User.dto.SignUpRequestDto;
import com.dpu.User.dto.SignUpResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    // 회원가입
    @PostMapping("/signup")
    public ResponseEntity<SignUpResponseDto> signUp(@RequestBody SignUpRequestDto request) {
        // TODO: userService.signUp(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto request) {
        // TODO: userService.login(request);
        return ResponseEntity.ok().build();
    }
}