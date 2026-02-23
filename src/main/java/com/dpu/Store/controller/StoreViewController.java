package com.dpu.Store.controller;

import com.dpu.Store.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/stores")  // RESTful하게 복수형 추천
@RequiredArgsConstructor
public class StoreViewController {

    private final StoreService storeService;

    // 가게 목록
    @GetMapping
    public String storeList(Model model) {
        // model.addAttribute("stores", storeService.findAll());
        return "store/list";
    }

    // 해당 가게 상세 페이지
    @GetMapping("/{storeId}")
    public String storeDetail(@PathVariable Long storeId, Model model) {
        model.addAttribute("storeId", storeId);
        // model.addAttribute("store", storeService.findById(storeId));
        return "store/detail";  // ❌ "store/{storeId}" 아님!
    }

    // 해당 가게 상품 목록
    @GetMapping("/{storeId}/products")
    public String storeProducts(@PathVariable Long storeId, Model model) {
        model.addAttribute("storeId", storeId);
        // model.addAttribute("products", productService.findByStoreId(storeId));
        return "store/products";
    }

    // 지도로 주변 가게 찾기
    @GetMapping("/map")
    public String storeMap(Model model) {
        return "store/map";
    }
}