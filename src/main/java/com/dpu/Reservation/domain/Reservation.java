package com.dpu.Reservation.domain;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "reservations")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //자동으로 키를 증가시킨다.
    private Long id;

    @Column(name = "pickup_time",nullable = false)
    private LocalDateTime pickTime;

    //주문 상태 ---> 픽업 준비
    @Enumerated(EnumType.STRING)
    @Column(nullable=false)
    private ReservationStatus status;


    @Column(name = "created_at",nullable = false,updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist(){
        if (this.createdAt == null) {
        this.createdAt = LocalDateTime.now();
      }
    }
}
