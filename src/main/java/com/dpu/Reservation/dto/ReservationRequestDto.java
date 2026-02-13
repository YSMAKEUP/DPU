package com.dpu.Reservation.dto;

import java.lang.reflect.Array;
import java.time.LocalDateTime;
import com.dpu.Reservation.domain.ReservationStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
//픽업 예약 dto
public class ReservationRequestDto {

    private Long customerId;
    private LocalDateTime pickTime;
    private  ReservationStatus status;
    private Array orderItem[]; //productId ,quantity 이렇게 필요한데 어떤 자료형으로 가져와야하는지 모르겠삼



}
