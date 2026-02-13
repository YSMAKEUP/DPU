//package com.dpu.Reservation.controller;
//
//import com.dpu.Reservation.dto.OrderItemDto;
////import com.dpu.Reservation.dto.OrderItemResponseDto;
////import com.dpu.Reservation.service.OrderItemService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/reservations/{reservationId}/order-items")
//@RequiredArgsConstructor
//public class OrderItemController {
//
//    private final OrderItemService orderItemService;
//
//    // 특정 예약의 주문 아이템 목록 조회
//    // GET /reservations/{reservationId}/order-items
//    @GetMapping
//    public ResponseEntity<List<OrderItemResponseDto>> getOrderItems(
//            @PathVariable Long reservationId) {
//        return ResponseEntity.ok(orderItemService.getOrderItems(reservationId));
//    }
//
//    // 주문 아이템 추가
//    // POST /reservations/{reservationId}/order-items
//    @PostMapping
//    public ResponseEntity<OrderItemResponseDto> addOrderItem(
//            @PathVariable Long reservationId,
//            @RequestBody OrderItemDto request) {
//        return ResponseEntity.ok(orderItemService.addOrderItem(reservationId, request));
//    }
//
//    // 주문 아이템 삭제
//    // DELETE /reservations/{reservationId}/order-items/{orderItemId}
//    @DeleteMapping("/{orderItemId}")
//    public ResponseEntity<String> deleteOrderItem(@PathVariable Long orderItemId) {
//        orderItemService.deleteOrderItem(orderItemId);
//        return ResponseEntity.ok("success");
//    }
//}
