package com.dpu.Store.domain;

import com.dpu.User.domain.User;
import jakarta.persistence.*;
import java.time.LocalTime;

@Entity
@Table(name = "stores")
public class Store {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "store_name", nullable = false)
    private String name;

    @Column(nullable = false)
    private String address;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id",nullable = false)
    private User user;



    @Column(nullable = false)
    private double latitude;

    @Column(nullable = false)
    private double longitude;

    @Column(name = "kakao_place_id", nullable = false)
    private Long kakaoPlaceId;

    @Column(name = "open_time", nullable = false)
    private LocalTime openTime;

    @Column(name = "close_time", nullable = false)
    private LocalTime closeTime;

    // 1=월 ... 7=일 (NULL이면 정기휴무 없음)
    @Column(name = "closed_day")
    private Integer closedDay;

    // NULL이면 기본 30분(서비스 로직에서 처리)
    @Column(name = "pickup_cutoff_minutes")
    private Integer pickupCutoffMinutes;

    // ===== getter / setter =====

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public User getOwner(){
        return user;
    }

    public void setUser(User user){
        this.user = user;
    }


    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public Long getKakaoPlaceId() {
        return kakaoPlaceId;
    }

    public void setKakaoPlaceId(Long kakaoPlaceId) {
        this.kakaoPlaceId = kakaoPlaceId;
    }

    public LocalTime getOpenTime() {
        return openTime;
    }

    public void setOpenTime(LocalTime openTime) {
        this.openTime = openTime;
    }

    public LocalTime getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(LocalTime  closeTime) {
        this.closeTime = closeTime;
    }

    public Integer getClosedDay() {
        return closedDay;
    }

    public void setClosedDay(Integer closedDay) {
        this.closedDay = closedDay;
    }

    public Integer getPickupCutoffMinutes() {
        return pickupCutoffMinutes;
    }

    public void setPickupCutoffMinutes(Integer pickupCutoffMinutes) {
        this.pickupCutoffMinutes = pickupCutoffMinutes;
    }
}
