package com.dpu.Store.controller;

import com.dpu.Store.dto.StoreResponseDto;
import com.dpu.Store.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@RequestMapping("/stores")  // RESTful하게 복수형 추천
@Controller
@RequiredArgsConstructor
public class StoreViewController {

    private final StoreService storeService;

    // 가게 목록
    @GetMapping("/list")
    public String storeList(@RequestParam(required = false) String name, Model model) {
        if (name != null && !name.isBlank()) {
            model.addAttribute("stores", storeService.getStore(name));
        }
        return "list";
    }

    // 해당 가게 상세 페이지
   @GetMapping("/{storeId}")
    public String storeDetail(@PathVariable Long storeId, Model model) {
        model.addAttribute("store", storeService.getStoreById(storeId));
        return "detail";
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