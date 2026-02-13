package com.dpu.Store.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/store")
public class StoreViewController {

    // 가게 목록 페이지
    @GetMapping("/list")
    public String storeListPage() {
        return "store/list";
    }

    // 가게 상세 페이지
    @GetMapping("/{storeId}")
    public String storeDetailPage(@PathVariable Long storeId, Model model) {
        model.addAttribute("storeId", storeId);
        return "store/detail";
    }
}