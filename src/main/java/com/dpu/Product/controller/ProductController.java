package com.dpu.Product.controller;

import com.dpu.Product.domain.Product;
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
        return "form";
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

    // 상품 수정 처리
    @PostMapping("/products/{id}")
    public String productUpdate(@PathVariable Long id,
                                @RequestParam Long storeId,
                                @RequestParam int price,
                                @RequestParam int stockQuantity,
                                @RequestParam(defaultValue = "false") boolean soldOut) {
        productService.updateProduct(id, storeId, price, stockQuantity, soldOut);
        return "redirect:/dashboard?updated=true";
    }

    // 상품 삭제
    @PostMapping("/products/{id}/delete")
    public String productDelete(@PathVariable Long id) {
        Long storeId = productService.deleteProduct(id);
        return "redirect:/stores/" + storeId;
    }
}