package com.dpu.Reservation.repository;

import com.dpu.Reservation.domain.Reservation;
import com.dpu.Reservation.domain.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {


}
