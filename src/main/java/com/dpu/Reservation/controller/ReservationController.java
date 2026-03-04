package com.dpu.Reservation.controller;

import com.dpu.Reservation.dto.MyReservationResponseDto;
import com.dpu.Reservation.dto.ReservationRequestDto;
import com.dpu.Reservation.dto.ReservationResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.dpu.Reservation.service.ReservationService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/reservation")
public class ReservationController {
    private final ReservationService reservationService;
    // 픽업 예약 생성
    @PostMapping
    public ResponseEntity<ReservationResponseDto> createReservation(
            @RequestBody ReservationRequestDto request) {
        ReservationResponseDto response = reservationService.createReservation(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // 내 예약 내역 단건 조회
    @GetMapping("/{reservationId}")
    public ResponseEntity<MyReservationResponseDto> getMyReservation(
            @PathVariable Long reservationId) {
        MyReservationResponseDto response= reservationService.getMyReservation(reservationId);
        return ResponseEntity.ok(response);
    }
}