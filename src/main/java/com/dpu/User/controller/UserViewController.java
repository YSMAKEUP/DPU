package com.dpu.User.controller;

import com.dpu.User.dto.LoginRequestDto;
import com.dpu.User.dto.LoginResponseDto;
import com.dpu.User.dto.SignUpRequestDto;
import com.dpu.User.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class UserViewController {
    private final UserService userService;

    // 회원가입 페이지 (파일명이 signup.html인지 확인 필수!)
    @GetMapping("/signup")
    public String signupPage(){
        return "signup"; // 앞에 '/' 제거
    }

    // 회원가입 처리
    @PostMapping("/signup")
    public String signup(@ModelAttribute SignUpRequestDto requestDto){
        userService.signUp(requestDto);
        return "redirect:/login";
    }

    // 로그인 페이지 (login.html 매핑)
    @GetMapping("/login")
    public String loginPage(){
        return "login"; // templates/login.html 호출
    }

    // 로그인 처리
    @PostMapping("/login")
    public String login(@ModelAttribute LoginRequestDto requestDto, HttpSession session){
        LoginResponseDto user = userService.login(requestDto);
        if (user != null) {
            session.setAttribute("loginUser", user);
            return "redirect:/";
        }
        return "redirect:/login?error"; // 로그인 실패 시 에러 파라미터 전달
    }

    // 로그아웃
    @GetMapping("/logout")
    public String logout(HttpSession session){
        session.invalidate();
        return "redirect:/login";
    }

    // 마이페이지
    @GetMapping("/mypage")
    public String myPage(HttpSession session, Model model){
        LoginResponseDto user = (LoginResponseDto) session.getAttribute("loginUser");
        if (user == null) {
            return "redirect:/login";
        }
        model.addAttribute("user", user);
        return "mypage"; // 앞에 '/' 제거
    }
}