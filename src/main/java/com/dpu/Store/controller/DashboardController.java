package com.dpu.Store.controller;

import com.dpu.Store.domain.Store;
import com.dpu.Store.repository.StoreRepository;
import com.dpu.User.domain.Role;
import com.dpu.User.dto.LoginResponseDto;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class DashboardController {

    private final StoreRepository storeRepository;

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        LoginResponseDto loginUser = (LoginResponseDto) session.getAttribute("loginUser");
        if (loginUser == null || loginUser.getRole() != Role.OWNER) return "redirect:/login";

        List<Store> stores = storeRepository.findByOwner_Id(loginUser.getUserId());

        if (stores.isEmpty()) {
            return "redirect:/stores/new"; // 예외 없이 바로 redirect
        }

        model.addAttribute("store", stores.get(0));
        model.addAttribute("loginUser", loginUser);
        return "dashboard";
    }
}