package com.dpu.Store.domain;


import jakarta.persistence.*;

@Entity
@Table(name = "stores")
public class Store {

    //매장 아이디
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //자동으로 키를 증가시킨다.
    private Long id;

    //매장 이름
    @Column(name = "store_name",nullable = false)
    private String name;

    //매장 위치
    @Column(nullable = false)
    private String address;

    //매장 위치
    @Column(nullable = false)
    private double latitude;

    @Column(nullable = false)
    private double longitude;

    @Column(name = "kakao_place_id", nullable = false)
    private Long kakaoplaceID;


    //매장에서 서비스 가입 날짜.

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;

    }

    public  String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getAddress(){
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude){
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public Long getKakaoplaceID() {
        return kakaoplaceID;
    }

    public void setKakaoplaceID(Long kakaoplaceID) {
        this.kakaoplaceID = kakaoplaceID;
    }
}
