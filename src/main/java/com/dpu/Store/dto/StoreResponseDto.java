package com.dpu.Store.dto;
import com.dpu.Product.dto.ProductDto;
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
    private List<ProductDto> products; // Product 엔티티 -> ProductDto
    private double latitude;  // long -> double
    private double longitude;



}
