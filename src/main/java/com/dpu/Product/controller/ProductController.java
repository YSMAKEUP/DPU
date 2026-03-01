package com.dpu.Product.controller;

import com.dpu.Product.dto.ProductDto;
import com.dpu.Product.dto.ProductResponseDto;
import com.dpu.Product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    // 특정 가게의 상품 목록 조회
    @GetMapping
    public ResponseEntity<ProductResponseDto> getProductsByStore(
            @RequestParam Long storeId) {
        ProductResponseDto response = productService.getProductsByStore(storeId);
        return ResponseEntity.ok(response);
    }

    // 특정 상품 단건 조회
    @GetMapping("/{productId}")
    public ResponseEntity<ProductDto> getProduct(@PathVariable Long productId) {
        ProductDto response=  productService.findProductDto(productId);
        return ResponseEntity.ok(response);
    }
}