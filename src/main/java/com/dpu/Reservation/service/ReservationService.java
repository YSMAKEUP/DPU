package com.dpu.Reservation.service;
import com.dpu.Reservation.domain.Reservation;

import com.dpu.Reservation.domain.ReservationStatus;
import com.dpu.Reservation.dto.MyReservationResponseDto;
import com.dpu.Reservation.dto.OrderItemResponseDto;
import com.dpu.Reservation.dto.ReservationRequestDto;
import com.dpu.Reservation.dto.ReservationResponseDto;
import com.dpu.Reservation.repository.ReservationRepository;
import com.dpu.User.domain.User;
import com.dpu.User.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.Id;
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

    @Transactional
    public Reservation createReservation(Reservation reservation) {

        return reservationRepository.save(reservation);


    }

    @Transactional
    //예약 조회
    public Reservation getReservation(Long reservationId) {

        return reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않은 예약입니다."));
    }

    //내 예약 조회

    @Transactional
    public List<Reservation> getUserId(Long userId) {

        return reservationRepository.findByUser_Id(userId);

    }

    //특정 상품 조회

    @Transactional
    public List<Reservation> getProductId(Long productId) {
        return reservationRepository.findByProductId(productId);
    }

    //사용자 예약 상태

    @Transactional
    public List<Reservation> getUserStatus(Long userId, ReservationStatus status) {
        return reservationRepository.findByUser_IdAndStatus(userId, status);
    }

    //예약 취소
    @Transactional
    public void deleteReservation(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new EntityNotFoundException("해당 예약이 존재하지 않습니다. id=" + reservationId));

        reservationRepository.delete(reservation);
    }


    // 기존 메서드들 그대로 유지...

    // 추가 1 - DTO로 예약 생성
    @Transactional
    public ReservationResponseDto createReservation(ReservationRequestDto request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));

        Reservation reservation = new Reservation();
        reservation.setUser(user);
        reservation.setPickupTime(request.getPickTime());
        reservation.setStatus(ReservationStatus.received);

        Reservation saved = reservationRepository.save(reservation);

        return new ReservationResponseDto(
                saved.getId(),
                saved.getUser().getId(),
                saved.getPickupTime(),
                saved.getStatus(),
                0,
                saved.getCreatedAt()
        );
    }

    // 추가 2 - DTO로 예약 단건 조회
    public MyReservationResponseDto getMyReservation(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 예약입니다."));

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

