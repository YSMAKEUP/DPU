//package com.dpu.Reservation.repository;
//
//import com.dpu.Reservation.domain.OrderItem;
//import org.springframework.data.jpa.repository.JpaRepository;
//
//import java.util.List;
//
//public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
//
//    // 특정 예약의 주문 아이템 전체 조회
//    List<OrderItem> findByReservation_ReservationId(Long reservationId);
//
//    // 특정 상품이 포함된 주문 아이템 조회
//    List<OrderItem> findByProduct_ProductId(Long productId);
//}