//package com.dpu.Reservation.service;
//
//import com.dpu.Product.domain.Product;
//import com.dpu.Product.repository.ProductRepository;
//import com.dpu.Reservation.domain.OrderItem;
//import com.dpu.Reservation.domain.Reservation;
//import com.dpu.Reservation.dto.OrderItemDto;
//import com.dpu.Reservation.dto.OrderItemResponseDto;
////import com.dpu.Reservation.repository.OrderItemRepository;
//import com.dpu.Reservation.repository.ReservationRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Service
//@RequiredArgsConstructor
//public class OrderItemService {
//
//    private final OrderItemRepository orderItemRepository;
//    private final ReservationRepository reservationRepository;
//    private final ProductRepository productRepository;
//
//    // 특정 예약의 주문 아이템 목록 조회
//    public List<OrderItemResponseDto> getOrderItems(Long reservationId) {
//        List<OrderItem> orderItems = orderItemRepository
//                .findByReservation_ReservationId(reservationId);
//
//        return orderItems.stream()
//                .map(item -> new OrderItemResponseDto(
//                        item.getProduct().getProductName(),
//                        item.getQuantity(),
//                        item.getPrice()
//                ))
//                .collect(Collectors.toList());
//    }
//
//    // 주문 아이템 추가
//    @Transactional
//    public OrderItemResponseDto addOrderItem(Long reservationId, OrderItemDto request) {
//        Reservation reservation = reservationRepository.findById(reservationId)
//                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 예약입니다."));
//
//        Product product = productRepository.findById(request.getProductId())
//                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품입니다."));
//
//        // 재고 확인
//        if (product.getStockQuantity() < request.getQuantity()) {
//            throw new IllegalStateException("재고가 부족합니다.");
//        }
//
//        OrderItem orderItem = OrderItem.builder()
//                .reservation(reservation)
//                .product(product)
//                .quantity(request.getQuantity())
//                .price(product.getPrice())
//                .build();
//
//        OrderItem saved = orderItemRepository.save(orderItem);
//
//        return new OrderItemResponseDto(
//                saved.getProduct().getProductName(),
//                saved.getQuantity(),
//                saved.getPrice()
//        );
//    }
//
//    // 주문 아이템 삭제
//    @Transactional
//    public void deleteOrderItem(Long orderItemId) {
//        OrderItem orderItem = orderItemRepository.findById(orderItemId)
//                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 주문 아이템입니다."));
//
//        orderItemRepository.delete(orderItem);
//    }
//}