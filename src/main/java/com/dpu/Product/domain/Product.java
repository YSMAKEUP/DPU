package com.dpu.Product.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Product {

    //product_id,name,price,재고량, 솔드아웃, dateTime

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //자동으로 키를 증가시킨다.
    private Long id;

    private String name;

    private int price;

    private int Sold_out ;
    // 생성날짜는 어떻게 불러오는가?

    //
    public Long getId(){
        return id;

    }
    public void setId(Long id){
        this.id = id;
    }

    //이름
    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price){
        this.price =price;
    }


    //선택권이 있을 때 어떻게 만드는지 설정하기
    public int getSold_out(){
        return Sold_out;
    }

    public void setSold_out(int Sold_out){
        this.Sold_out =Sold_out;
    }
}
