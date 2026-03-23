package com.dpu.Reservation.service;

import com.dpu.Product.domain.Product;
import com.dpu.Product.repository.ProductRepository;
import com.dpu.Product.service.ProductService;
import com.dpu.Reservation.domain.OrderItem;
import com.dpu.Reservation.domain.Reservation;
import com.dpu.Reservation.domain.ReservationStatus;
import com.dpu.Reservation.dto.*;
import com.dpu.Reservation.repository.OrderItemRepository;
import com.dpu.Reservation.repository.ReservationRepository;
import com.dpu.Store.domain.Store;
import com.dpu.Store.repository.StoreRepository;
import com.dpu.User.domain.User;
import com.dpu.User.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;
    private final StoreRepository storeRepository;
    private final ProductService productService;

    // 공통 - 현재 로그인 유저 조회
    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));
    }

    // 공통 - 본인 예약 검증
    private void validateReservationOwner(Reservation reservation, User currentUser) {
        if (!reservation.getUser().getId().equals(currentUser.getId())) {
            throw new IllegalArgumentException("본인의 예약이 아닙니다.");
        }
    }

    // 예약 생성
    @Transactional
    public ReservationResponseDto createReservation(ReservationRequestDto request) {
        User user = getCurrentUser();

        Reservation reservation = new Reservation();
        reservation.setUser(user);
        reservation.setPickupTime(request.getPickTime());
        reservation.setStatus(ReservationStatus.RECEIVED);

        // 첫 번째 상품에서 store 자동 추출
        // ✅ 핵심 수정: firstProduct.getStore()는 em.clear() 이후 LAZY 프록시라
        //    TransientObjectException 발생 → storeRepository로 직접 재조회
        if (request.getOrderItems() != null && !request.getOrderItems().isEmpty()) {
            Product firstProduct = productRepository.findById(
                            request.getOrderItems().get(0).getProductId())
                    .orElseThrow(() -> new RuntimeException("상품을 찾을 수 없습니다."));

            Long storeId = firstProduct.getStore().getId();
            Store store = storeRepository.findById(storeId)
                    .orElseThrow(() -> new RuntimeException("가게를 찾을 수 없습니다."));
            reservation.setStore(store);
        }

        Reservation saved = reservationRepository.save(reservation);

        if (request.getOrderItems() != null) {
            for (OrderItemDto item : request.getOrderItems()) {
                Product product = productRepository.findById(item.getProductId())
                        .orElseThrow(() -> new RuntimeException("상품을 찾을 수 없습니다."));

                OrderItem orderItem = OrderItem.builder()
                        .reservation(saved)
                        .product(product)
                        .quantity(item.getQuantity())
                        .price(product.getPrice())
                        .build();

                orderItemRepository.save(orderItem);

                // 재고 감소
                productService.decreaseStock(item.getProductId(), item.getQuantity());
            }
        }

        return new ReservationResponseDto(
                saved.getId(),
                saved.getUser().getId(),
                saved.getPickupTime(),
                saved.getStatus(),
                0,
                saved.getCreatedAt()
        );
    }

    // 예약 단건 조회
    @Transactional
    public Reservation getReservation(Long reservationId) {
        return reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않은 예약입니다."));
    }

    // 내 예약 조회
    @Transactional
    public List<Reservation> getUserId(Long userId) {
        return reservationRepository.findByUser_Id(userId);
    }

    // 특정 상품 조회
    @Transactional
    public List<Reservation> getProductId(Long productId) {
        return reservationRepository.findByProductId(productId);
    }

    // 사용자 예약 상태 조회
    @Transactional
    public List<Reservation> getUserStatus(Long userId, ReservationStatus status) {
        return reservationRepository.findByUser_IdAndStatus(userId, status);
    }

    // 예약 취소
    @Transactional
    public void deleteReservation(Long reservationId) {
        User currentUser = getCurrentUser();

        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new EntityNotFoundException("해당 예약이 존재하지 않습니다. id=" + reservationId));

        validateReservationOwner(reservation, currentUser);

        for (OrderItem item : reservation.getOrderItems()) {
            productService.restoreStock(item.getProduct().getId(), item.getQuantity());
        }

        orderItemRepository.deleteAll(reservation.getOrderItems());
        reservationRepository.delete(reservation);
    }

    // 내 예약 단건 조회
    @Transactional
    public MyReservationResponseDto getMyReservation(Long reservationId) {
        User currentUser = getCurrentUser();

        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 예약입니다."));

        validateReservationOwner(reservation, currentUser);

        return toMyReservationResponseDto(reservation);
    }

    // 내 전체 예약 목록 조회
    @Transactional
    public List<MyReservationResponseDto> getMyReservations(Long userId) {
        return reservationRepository.findByUser_Id(userId).stream()
                .map(this::toMyReservationResponseDto)
                .collect(Collectors.toList());
    }

    // 사장님 - 가게 전체 예약 조회
    @Transactional
    public List<MyReservationResponseDto> getStoreReservations(Long storeId) {
        return reservationRepository.findByStoreId(storeId).stream()
                .map(this::toMyReservationResponseDto)
                .collect(Collectors.toList());
    }

    // 사장님 - 가게 상태별 예약 조회
    @Transactional
    public List<MyReservationResponseDto> getStoreReservationsByStatus(Long storeId, ReservationStatus status) {
        return reservationRepository.findByStoreIdAndStatus(storeId, status).stream()
                .map(this::toMyReservationResponseDto)
                .collect(Collectors.toList());
    }

    // 사장님 - 예약 상태 변경
    @Transactional
    public void updateReservationStatus(Long reservationId, ReservationStatus status) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new EntityNotFoundException("해당 예약이 존재하지 않습니다."));
        reservation.setStatus(status);
    }

    // 공통 - Reservation → MyReservationResponseDto 변환
    private MyReservationResponseDto toMyReservationResponseDto(Reservation reservation) {
        List<OrderItemResponseDto> orderItems = reservation.getOrderItems().stream()
                .map(item -> new OrderItemResponseDto(
                        item.getProduct().getProductName(),
                        item.getQuantity(),
                        item.getPrice()
                ))
                .collect(Collectors.toList());

        String storeName = reservation.getOrderItems().isEmpty() ? "알 수 없음"
                : reservation.getOrderItems().get(0).getProduct().getStore().getName();

        return new MyReservationResponseDto(
                reservation.getId(),
                storeName,
                reservation.getPickupTime(),
                reservation.getStatus(),
                orderItems,
                reservation.getTotalAmount()
        );
    }
}