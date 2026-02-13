package com.dpu.User.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/user")
public class UserViewController {

    // 로그인 페이지
    @GetMapping("/login")
    public String loginPage() {
        return "user/login";
    }

    // 회원가입 페이지
    @GetMapping("/signup")
    public String signupPage() {
        return "user/singup";
    }

    // 마이페이지
    @GetMapping("/mypage")
    public String myPage() {
        return "user/mypage";
    }
}