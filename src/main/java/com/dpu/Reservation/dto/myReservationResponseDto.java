package com.dpu.Reservation.dto;
import com.dpu.Reservation.domain.ReservationStatus;

import java.lang.reflect.Array;
import java.time.LocalDateTime;

public class myReservationResponseDto {

    private Long reservationId;
    private Long customerId;
    private LocalDateTime picktime;
    private ReservationStatus status;
    private Array[] orderItem;
    private int totalAmount;



}
