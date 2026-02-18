package com.dpu.Reservation.repository;

import com.dpu.Reservation.domain.Reservation;
import com.dpu.Reservation.domain.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    // 픽업 예약, 예약 내역 조회, 예약 삭제
    // 전체 조회, 예약 삭제 등 같은 경우에는 이미 JPA에서 기본으로 제공함

    // 특정 조회 - 특정 사용자의 예약, 특정 상품 예약, 특정 상태 예약

    // 특정 사용자의 전체 예약 조회
    List<Reservation> findByUser_Id(Long userId);

    // 특정 상품의 전체 예약 조회
    List<Reservation> findByProduct_Id(Long productId);

    // 특정 사용자의 특정 상태 예약 조회 (예: 진행중인 예약만)
    List<Reservation> findByUser_IdAndStatus(Long userId, ReservationStatus status);


}