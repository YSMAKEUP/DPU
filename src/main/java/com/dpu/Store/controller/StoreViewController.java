package com.dpu.Store.controller;

import com.dpu.Store.domain.Store;
import com.dpu.Store.dto.StoreCreateRequestDto;
import com.dpu.Store.repository.StoreRepository;
import com.dpu.Store.service.StoreService;
import com.dpu.User.domain.Role;
import com.dpu.User.dto.LoginResponseDto;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/stores")
@Controller
@RequiredArgsConstructor
public class StoreViewController {

    private final StoreService storeService;
    private final StoreRepository storeRepository;

    // 가게 목록
    @GetMapping("/list")
    public String storeList(@RequestParam(required = false) String name, Model model) {
        if (name != null && !name.isBlank()) {
            model.addAttribute("stores", storeService.getStore(name));
        }
        return "list";
    }

    // 매장 등록 폼
    @GetMapping("/new")
    public String storeForm(HttpSession session, Model model) {
        LoginResponseDto loginUser = (LoginResponseDto) session.getAttribute("loginUser");
        if (loginUser == null || loginUser.getRole() != Role.OWNER) return "redirect:/login";

        model.addAttribute("storeForm", new StoreCreateRequestDto());
        return "Store_new";
    }

    // 매장 등록 처리
    @PostMapping("/new")
    public String storeCreate(HttpSession session,
                              @ModelAttribute("storeForm") StoreCreateRequestDto storeForm) {
        LoginResponseDto loginUser = (LoginResponseDto) session.getAttribute("loginUser");
        if (loginUser == null || loginUser.getRole() != Role.OWNER) return "redirect:/login";

        storeService.createStore(storeForm, loginUser.getUserId());
        return "redirect:/dashboard";
    }

    // 해당 가게 상세 페이지
    @GetMapping("/{storeId}")
    public String storeDetail(@PathVariable Long storeId, Model model) {
        model.addAttribute("store", storeService.getStoreById(storeId));
        return "store_detail";
    }

    // 해당 가게 상품 목록
    @GetMapping("/{storeId}/products")
    public String storeProducts(@PathVariable Long storeId, Model model) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new IllegalArgumentException("매장 없음"));
        model.addAttribute("store", store);
        model.addAttribute("products", store.getProducts());
        model.addAttribute("storeId", storeId);
        return "products";
    }
    // 지도로 주변 가게 찾기
    @GetMapping("/map")
    public String storeMap(Model model) {
        return "store/map";
    }
}