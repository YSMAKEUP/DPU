package com.dpu.Store.dto;

import com.dpu.Product.domain.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StoreResponseDto {

    private Long storeId;
    private String storeName;
    private List<Product> products;
    private long latitude;
    private long longitude;



}
