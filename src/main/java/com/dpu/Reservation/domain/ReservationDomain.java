package com.dpu.Reservation.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
public class ReservationDomain {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //자동으로 키를 증가시킨다.
    private Long id;

    private DateTimeFormat pickTime;

    private boolean status;

    private DateTimeFormat created_at;
}
