package com.dpu.Reservation.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemDto {
    private Long productId;   // 어떤 상품인지
    private int quantity;     // 몇 개 주문하는지
}