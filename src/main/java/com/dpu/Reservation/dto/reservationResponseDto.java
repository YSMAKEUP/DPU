package com.dpu.Reservation.dto;

import java.time.LocalDateTime;

import com.dpu.Reservation.domain.ReservationStatus;



//픽업 예약 dto
public class reservationResponseDto {

    private Long reservationId;
    private Long customerId;
    private LocalDateTime pickTime;
    private  ReservationStatus status;
    private  int totalAmount;
    private LocalDateTime createdAt;

}
