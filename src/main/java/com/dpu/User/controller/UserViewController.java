package com.dpu.User.controller;

import com.dpu.User.domain.User;
import com.dpu.User.dto.LoginResponseDto;
import com.dpu.User.dto.SignUpRequestDto;
import com.dpu.User.dto.UserUpdateDto;
import com.dpu.User.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class UserViewController {
    private final UserService userService;

    // 회원가입 페이지
    @GetMapping("/signup")
    public String signupPage() {
        return "signup";
    }

    // 회원가입 처리
    @PostMapping("/signup")
    public String signup(@ModelAttribute SignUpRequestDto requestDto) {
        userService.signUp(requestDto);
        return "redirect:/login";
    }

    // 로그인 페이지
    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    // ✅ POST /login 은 Spring Security가 처리 → 여기서 따로 작성 안 함

    // 마이페이지
    @GetMapping("/mypage")
    public String myPage(Principal principal, Model model) {
        if (principal == null) return "redirect:/login";

        User user = userService.findByEmail(principal.getName());
        LoginResponseDto dto = LoginResponseDto.builder()
                .result("success")
                .userId(user.getId())
                .role(user.getRole())
                .name(user.getName())
                .email(user.getEmail())
                .build();

        model.addAttribute("user", dto);
        return "mypage";
    }

    // 회원정보 수정 폼
    @GetMapping("/user_edit")
    public String editForm(Principal principal, Model model) {
        if (principal == null) return "redirect:/login";

        User user = userService.findByEmail(principal.getName());
        UserUpdateDto dto = new UserUpdateDto();
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());

        model.addAttribute("userForm", dto);
        return "user_edit";
    }

    // 회원정보 수정 처리
    @PostMapping("/user_edit")
    public String edit(Principal principal,
                       @ModelAttribute("userForm") UserUpdateDto dto,
                       RedirectAttributes redirectAttributes) {
        if (principal == null) return "redirect:/login";

        User user = userService.findByEmail(principal.getName());

        try {
            userService.updateUser(user.getId(), dto);
            redirectAttributes.addFlashAttribute("msg", "회원정보가 수정되었습니다.");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/user_edit";
    }

    // 회원 탈퇴
    @PostMapping("/user_delete")
    public String deleteUser(Principal principal, HttpSession session) {
        if (principal == null) return "redirect:/login";

        User user = userService.findByEmail(principal.getName());
        userService.deleteUser(user.getId());
        session.invalidate();
        return "redirect:/login";
    }
}