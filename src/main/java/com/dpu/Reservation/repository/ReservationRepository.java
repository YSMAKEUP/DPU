package com.dpu.Reservation.repository;

import com.dpu.Reservation.domain.Reservation;
import com.dpu.Reservation.domain.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    // 특정 사용자의 전체 예약 조회
    List<Reservation> findByUser_Id(Long userId);

    // 특정 상품의 전체 예약 조회
    @Query("""
    select distinct r
    from Reservation r
    join r.orderItems oi
    where oi.product.id = :productId
""")
    List<Reservation> findByProductId(Long productId);

    // 특정 사용자의 특정 상태 예약 조회
    List<Reservation> findByUser_IdAndStatus(Long userId, ReservationStatus status);

    // ✅ 특정 가게의 전체 예약 조회 (사장님용)
    @Query("""
    select distinct r
    from Reservation r
    join r.orderItems oi
    where oi.product.store.id = :storeId
""")
    List<Reservation> findByStoreId(Long storeId);

    // ✅ 특정 가게의 특정 상태 예약 조회 (사장님용)
    @Query("""
    select distinct r
    from Reservation r
    join r.orderItems oi
    where oi.product.store.id = :storeId
    and r.status = :status
""")
    List<Reservation> findByStoreIdAndStatus(Long storeId, ReservationStatus status);
}