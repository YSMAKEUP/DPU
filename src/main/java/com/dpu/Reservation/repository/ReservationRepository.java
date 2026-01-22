package com.dpu.Reservation.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dpu.Reservation.domain.Reservation;

public interface ReservationRepository extends JpaRepository<Reservation,Long> {




}
