package com.dpu.Product.domain;

import com.dpu.Store.domain.Store;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "products")
public class Product {

    //product_id,name,price,재고량, 솔드아웃, dateTime

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //자동으로 키를 증가시킨다.
    @Column(name = "product_id")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int price;

    @Column(name = "stock_qty", nullable = false)
    private int quantity;


    @Column(name = "is_sold_out", nullable = false)
    private boolean soldOut ;


    @Column(name = "created_at",nullable = false,updatable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id",nullable = false)
    private Store store;

    public Store getStore(){
        return store;
    }

    public void setStore(Store store){
        this.store = store ;
    }

    @PrePersist
    public  void prePersist(){
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
    }

    //getters/setters
    public Long getId(){
        return id;
    }
    public void setId(Long id){
        this.id = id;
    }
    //이름 get/set
    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name = name;
    }

    //가격 get/set
    public int getPrice() {
        return price;
    }

    public void setPrice(int price){
        this.price =price;
    }


    //재고
    public int getQuantity(){
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    //Sold-out getter/setter
    public boolean isSoldOut(){
        return soldOut;
    }

    public void setSoldOut(boolean soldOut){
        this.soldOut= soldOut;
    }


    //생성 시간 getter/setter
    public LocalDateTime getCreatedAt(){
        return  createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }


    public String getProductName() {
        return name;
    }

    public int getStockQuantity() {
        return quantity;
    }
}
