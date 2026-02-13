package com.dpu.Product.controller;

import com.dpu.Product.dto.ProductDto;
import com.dpu.Product.dto.ProductResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    // 특정 가게의 상품 목록 조회
    @GetMapping
    public ResponseEntity<ProductResponseDto> getProductsByStore(
            @RequestParam Long storeId) {
        // TODO: productService.getProductsByStore(storeId);
        return ResponseEntity.ok().build();
    }

    // 특정 상품 단건 조회
    @GetMapping("/{productId}")
    public ResponseEntity<ProductDto> getProduct(@PathVariable Long productId) {
        // TODO: productService.getProduct(productId);
        return ResponseEntity.ok().build();
    }
}