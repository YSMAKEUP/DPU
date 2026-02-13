package com.dpu.Reservation.dto;

import java.time.LocalDateTime;

import com.dpu.Reservation.domain.ReservationStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
//픽업 예약 dto
public class ReservationResponseDto {

    private Long reservationId;
    private Long customerId;
    private LocalDateTime pickTime;
    private  ReservationStatus status;
    private  int totalAmount;
    private LocalDateTime createdAt;

}
