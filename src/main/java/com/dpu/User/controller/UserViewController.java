package com.dpu.User.controller;

import com.dpu.User.domain.User;
import com.dpu.User.dto.LoginRequestDto;
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

@Controller
@RequiredArgsConstructor
public class UserViewController {
    private final UserService userService;

    // 회원가입 페이지
    @GetMapping("/signup")
    public String signupPage(){
        return "signup";
    }

    // [BUG FIX] 불필요한 @RequestParam 제거 - requestDto에 이미 모든 값이 담겨있음
    @PostMapping("/signup")
    public String signup(@ModelAttribute SignUpRequestDto requestDto){
        userService.signUp(requestDto);
        return "redirect:/login";
    }

    // 로그인 페이지
    @GetMapping("/login")
    public String loginPage(){
        return "login";
    }

    // 로그인 처리
    @PostMapping("/login")
    public String login(@ModelAttribute LoginRequestDto requestDto, HttpSession session){
        LoginResponseDto user = userService.login(requestDto);
        if (user != null) {
            session.setAttribute("loginUser", user);
            return "redirect:/";
        }
        return "redirect:/login?error";
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
        return "mypage";
    }

    // 회원정보 수정 폼
    @GetMapping("/user_edit")
    public String editForm(HttpSession session, Model model) {
        LoginResponseDto loginUser = (LoginResponseDto) session.getAttribute("loginUser");
        if (loginUser == null)
            return "redirect:/login";

        UserUpdateDto dto = new UserUpdateDto();
        dto.setName(loginUser.getName());
        dto.setEmail(loginUser.getEmail());

        model.addAttribute("userForm", dto);
        return "user_edit";
    }

    // 회원정보 수정 처리
    @PostMapping("/user_edit")
    public String edit(HttpSession session,
                       @ModelAttribute("userForm") UserUpdateDto dto,
                       RedirectAttributes redirectAttributes) {

        LoginResponseDto loginUser = (LoginResponseDto) session.getAttribute("loginUser");
        if (loginUser == null) return "redirect:/login";

        try {
            userService.updateUser(loginUser.getUserId(), dto);

            // 세션 최신화
            User updatedUser = userService.findById(loginUser.getUserId());
            LoginResponseDto updatedDto = LoginResponseDto.builder()
                    .result("success")
                    .userId(updatedUser.getId())
                    .role(updatedUser.getRole())
                    .name(updatedUser.getName())
                    .email(updatedUser.getEmail())
                    .build();
            session.setAttribute("loginUser", updatedDto);

            redirectAttributes.addFlashAttribute("msg", "회원정보가 수정되었습니다.");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/user_edit";
    }

    // 회원 탈퇴
    @PostMapping("/user_delete")
    public String deleteUser(HttpSession session) {
        LoginResponseDto loginUser = (LoginResponseDto) session.getAttribute("loginUser");
        if (loginUser == null) return "redirect:/login";

        userService.deleteUser(loginUser.getUserId());
        session.invalidate();
        return "redirect:/login";
    }
}