package com.dpu.Product.controller;

import com.dpu.Product.dto.ProductDto;
import com.dpu.Product.dto.ProductRequestDto;
import com.dpu.Product.service.ProductService;
import com.dpu.Store.domain.Store;
import com.dpu.Store.repository.StoreRepository;
import com.dpu.User.domain.Role;
import com.dpu.User.dto.LoginResponseDto;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final StoreRepository storeRepository;

    // 상품 등록 폼
    @GetMapping("/products/new")
    public String productForm(HttpSession session, Model model) {
        LoginResponseDto loginUser = (LoginResponseDto) session.getAttribute("loginUser");
        if (loginUser == null || loginUser.getRole() != Role.OWNER) return "redirect:/login";

        Store store = storeRepository.findByOwner_Id(loginUser.getUserId())
                .stream().findFirst()
                .orElseThrow(() -> new IllegalArgumentException("매장 정보가 없습니다."));

        model.addAttribute("product", new ProductRequestDto());
        model.addAttribute("storeId", store.getId());
        return "Store_new";
    }

    // 상품 등록 처리
    @PostMapping("/products")
    public String productCreate(HttpSession session,
                                @RequestParam Long storeId,
                                @RequestParam String productName,
                                @RequestParam int price,
                                @RequestParam int stockQuantity,
                                @RequestParam(defaultValue = "false") boolean soldOut) {
        LoginResponseDto loginUser = (LoginResponseDto) session.getAttribute("loginUser");
        if (loginUser == null || loginUser.getRole() != Role.OWNER) return "redirect:/login";

        productService.createProduct(storeId, productName, price, stockQuantity, soldOut);
        return "redirect:/stores/" + storeId;
    }

    // 상품 수정 폼
    @GetMapping("/products/{id}/edit")
    public String productEditForm(@PathVariable Long id, HttpSession session, Model model) {
        LoginResponseDto loginUser = (LoginResponseDto) session.getAttribute("loginUser");
        if (loginUser == null || loginUser.getRole() != Role.OWNER) return "redirect:/login";

        ProductDto product = productService.findProductDto(id);
        model.addAttribute("product", product);
        return "edit";
    }

    // [BUG FIX] 상품 수정 처리 - storeId를 클라이언트에서 받지 않고 세션 기반으로 검증
    @PostMapping("/products/{id}")
    public String productUpdate(@PathVariable Long id,
                                @RequestParam int price,
                                @RequestParam int stockQuantity,
                                @RequestParam(defaultValue = "false") boolean soldOut,
                                HttpSession session) {
        LoginResponseDto loginUser = (LoginResponseDto) session.getAttribute("loginUser");
        if (loginUser == null || loginUser.getRole() != Role.OWNER) return "redirect:/login";

        // 세션의 로그인 유저 소유 가게에서 storeId를 직접 조회 → 클라이언트 조작 불가
        Store store = storeRepository.findByOwner_Id(loginUser.getUserId())
                .stream().findFirst()
                .orElseThrow(() -> new IllegalArgumentException("매장 정보가 없습니다."));

        productService.updateProduct(id, store.getId(), price, stockQuantity, soldOut);
        return "redirect:/dashboard?updated=true";
    }

    // 상품 삭제 - 권한 검증
    @PostMapping("/products/{id}/delete")
    public String productDelete(@PathVariable Long id, HttpSession session) {
        LoginResponseDto loginUser = (LoginResponseDto) session.getAttribute("loginUser");
        if (loginUser == null || loginUser.getRole() != Role.OWNER) return "redirect:/login";

        Long storeId = productService.deleteProduct(id);
        return "redirect:/stores/" + storeId;
    }
}