package com.dpu.Reservation.service;
import com.dpu.Reservation.domain.Reservation;

import com.dpu.Reservation.domain.ReservationStatus;
import com.dpu.Reservation.repository.ReservationRepository;
import jakarta.persistence.Id;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;

    public ReservationService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

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
    public List<Reservation> getUserId(Long userId) {

        return reservationRepository.findByUser_Id(userId);

    }

    //특정 상품 조회
    public List<Reservation> getProductId(Long productId) {
        return reservationRepository.findByProduct_Id(productId);
    }

    //사용자 예약 상태

    public List<Reservation> getUserStatus(Long userId, ReservationStatus status) {
        return reservationRepository.findByUser_IdAndStatus(userId, status);
    }

    //예약 취쇠


}


