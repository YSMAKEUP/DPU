package com.dpu.Reservation.dto;
import com.dpu.Reservation.domain.ReservationStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MyReservationResponseDto {

    private Long reservationId;
    private String storeName;
    private LocalDateTime pickTime;
    private ReservationStatus status;
    private List<OrderItemResponseDto>orderItems;
    private int totalAmount;



}
