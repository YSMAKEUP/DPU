package com.dpu.Reservation.domain;

import org.springframework.format.annotation.DateTimeFormat;

public class ReservationDomain {

    private Long id;
    private DateTimeFormat pickTime;
    private boolean status;
    private DateTimeFormat created_at;
}
