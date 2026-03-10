package com.dpu.User.controller;

import com.dpu.User.domain.Role;
import com.dpu.User.dto.LoginResponseDto;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class AdminController {

    @GetMapping("/admin")
    public String adminPage(HttpSession session, Model model) {
        LoginResponseDto loginUser = (LoginResponseDto) session.getAttribute("loginUser");

        if (loginUser == null || loginUser.getRole() != Role.OWNER) {
            return "redirect:/login";
        }

        model.addAttribute("loginUser", loginUser);
        return "dashboard"; // templates/admin/dashboard.html
    }
}