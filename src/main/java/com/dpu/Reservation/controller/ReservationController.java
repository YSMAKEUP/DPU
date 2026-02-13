package com.dpu.Reservation.controller;

import com.dpu.Reservation.dto.MyReservationResponseDto;
import com.dpu.Reservation.dto.ReservationRequestDto;
import com.dpu.Reservation.dto.ReservationResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    // 픽업 예약 생성
    @PostMapping
    public ResponseEntity<ReservationResponseDto> createReservation(
            @RequestBody ReservationRequestDto request) {
        // TODO: reservationService.createReservation(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // 내 예약 내역 단건 조회
    @GetMapping("/{reservationId}")
    public ResponseEntity<MyReservationResponseDto> getMyReservation(
            @PathVariable Long reservationId) {
        // TODO: reservationService.getMyReservation(reservationId);
        return ResponseEntity.ok().build();
    }
}