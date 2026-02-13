package com.dpu.Product.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/product")
public class ProductViewController {

    // 상품 등록 폼 페이지
    @GetMapping("/form")
    public String productFormPage() {
        return "product/form";
    }

    // 상품 상세 페이지
    @GetMapping("/{productId}")
    public String productDetailPage(@PathVariable Long productId, Model model) {
        model.addAttribute("productId", productId);
        return "product/detail";
    }

    // 상품 수정 페이지
    @GetMapping("/{productId}/edit")
    public String productEditPage(@PathVariable Long productId, Model model) {
        model.addAttribute("productId", productId);
        return "product/edit";
    }
}