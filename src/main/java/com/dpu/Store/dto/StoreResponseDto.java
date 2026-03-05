package com.dpu.Store.dto;
import com.dpu.Product.dto.ProductDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StoreResponseDto {

    private Long storeId;
    private String storeName;
    private List<ProductDto> products; // Product 엔티티 -> ProductDto
    private double latitude;  // long -> double
    private double longitude;
    private String address;       // 추가
    private LocalTime openTime;   // 추가
    private LocalTime closeTime;  // 추가
    private Integer closedDay;    // 추가
    private boolean isOpen;

}
