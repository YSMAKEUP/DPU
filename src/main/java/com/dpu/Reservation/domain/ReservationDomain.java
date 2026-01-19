package com.dpu.Reservation.domain;

import com.dpu.User.domain.Role;
import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Entity
public class ReservationDomain {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //자동으로 키를 증가시킨다.
    private Long id;

    @DateTimeFormat
    @Column(name = "pickup_time")
    private LocalDateTime pickTime;


    //주문 상태 ---> 픽업 준비
    @Enumerated(EnumType.STRING)
    @Column(nullable=false)
    private reservation_status status;


    @DateTimeFormat
    @Column(name = "created_at")
    private LocalDateTime created_at;
}
