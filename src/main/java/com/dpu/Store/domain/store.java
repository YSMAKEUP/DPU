package com.dpu.Store.domain;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class store {

    //매장 아이디
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //자동으로 키를 증가시킨다.
    private Long id;

    //매장 이름
    private String name;

    //매장 위치


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


}
