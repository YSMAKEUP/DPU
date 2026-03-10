package com.dpu.Reservation.controller;

import com.dpu.Reservation.domain.ReservationStatus;
import com.dpu.Reservation.dto.MyReservationResponseDto;
import com.dpu.Reservation.dto.ReservationRequestDto;
import com.dpu.Reservation.dto.ReservationResponseDto;
import com.dpu.User.domain.Role;
import com.dpu.User.dto.LoginResponseDto;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.dpu.Reservation.service.ReservationService;

import java.util.List;

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

    // 내 예약 단건 조회
    @GetMapping("/{reservationId}")
    public ResponseEntity<MyReservationResponseDto> getMyReservation(
            @PathVariable Long reservationId) {
        MyReservationResponseDto response = reservationService.getMyReservation(reservationId);
        return ResponseEntity.ok(response);
    }

    // 예약 취소
    @DeleteMapping("/{reservationId}")
    public ResponseEntity<Void> deleteReservation(
            @PathVariable Long reservationId) {
        reservationService.deleteReservation(reservationId);
        return ResponseEntity.noContent().build();
    }

    // 사장님 - 가게 전체 예약 조회
    @GetMapping("/store/{storeId}")
    public ResponseEntity<List<MyReservationResponseDto>> getStoreReservations(
            @PathVariable Long storeId,
            HttpSession session) {
        LoginResponseDto loginUser = (LoginResponseDto) session.getAttribute("loginUser");
        if (loginUser == null || loginUser.getRole() != Role.OWNER) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok(reservationService.getStoreReservations(storeId));
    }

    // 사장님 - 가게 상태별 예약 조회
    @GetMapping("/store/{storeId}/status")
    public ResponseEntity<List<MyReservationResponseDto>> getStoreReservationsByStatus(
            @PathVariable Long storeId,
            @RequestParam ReservationStatus status,
            HttpSession session) {
        LoginResponseDto loginUser = (LoginResponseDto) session.getAttribute("loginUser");
        if (loginUser == null || loginUser.getRole() != Role.OWNER) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok(reservationService.getStoreReservationsByStatus(storeId, status));
    }

    // ✅ 사장님 - 예약 상태 변경
    @PatchMapping("/{reservationId}/status")
    public ResponseEntity<Void> updateReservationStatus(
            @PathVariable Long reservationId,
            @RequestParam ReservationStatus status,
            HttpSession session) {
        LoginResponseDto loginUser = (LoginResponseDto) session.getAttribute("loginUser");
        if (loginUser == null || loginUser.getRole() != Role.OWNER) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        reservationService.updateReservationStatus(reservationId, status);
        return ResponseEntity.ok().build();
    }
}