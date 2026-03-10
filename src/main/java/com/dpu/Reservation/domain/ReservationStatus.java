package com.dpu.Reservation.domain;

public enum ReservationStatus {
    RECEIVED,    // 픽업 주문 접수
    PREPARING,   // 준비 중
    READY,       // 픽업 가능
    PICKED_UP,   // 픽업 완료
    CANCELED     // 취소
}

//RECEIVED : 픽업 주문 접수
//
//PREPARING : 접수 중(준비 중)
//
//READY : 픽업 예약 완료(픽업 가능)
