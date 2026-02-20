package com.dpu.Product.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto{
        private Long productId;
        private String productName;
        private int price;
        private int stockQuantity;
        private boolean soldOut;
}




