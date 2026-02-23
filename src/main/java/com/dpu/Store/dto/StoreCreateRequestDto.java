package com.dpu.Store.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StoreCreateRequestDto {
    private String name;
    private String address;
    private double longitude;
    private double latitude;
    private Long kakaoPlaceId;
    private LocalTime openTime;
    private LocalTime closeTime;
    private Integer closedDay;
    private Integer pickupCutoffMinutes;
}