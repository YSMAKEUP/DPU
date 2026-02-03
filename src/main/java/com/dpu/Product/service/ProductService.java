package com.dpu.Product.service;
import com.dpu.Store.repository.StoreRepository;
import com.dpu.User.domain.Role;
import com.dpu.Product.domain.Product;
import com.dpu.Product.repository.ProductRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class ProductService {
    private  final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository){
        this.productRepository = productRepository;
    }
    //1. 상품 목록 조회(가게별, 특정 메뉴별), 상품 상세 조회
    public List<Product> findStore(Long storeId){
        return productRepository.findByStoreId(storeId);
    }

    //1-2 특정 메뉴별
    public Optional<Product> findProduct(Long id){
        return productRepository.findById(id);
    }



    //2. 상품 관리( 1.등록 2.수정 3. 삭제)
    //2-1 등록 일 단 상품이 살아있는지 없는지에 대해서 조회를 해야한다.

    public Long creatProduct(Long id,String name, int price ,int quantity ,boolean soldOut, LocalDateTime createdAt){
        if (productRepository.existsById(id))
            throw new IllegalArgumentException("이미 등록이 완료된 메뉴입니다.");

        Product product = new Product();
        product.setId(id);
        product.setName(name);
        product.setPrice(price);
        product.setQuantity(quantity);
        product.setSoldOut(soldOut);
        product.setCreatedAt(createdAt);
        productRepository.save(product);
        return  product.getId();
    }

    //2-2 수정

    public Long updateProduct(Long id,Long ownerId,int price ,int quantity ,boolean soldOut, LocalDateTime createdAt){
        //1. 가게가 먼저 있는지 2. 가게에 상품이 잇는지 조회

        Long store = productRepository.findById(id);




    }









    //3. 재고 조회 ,재고 수동 조정(증가 , 감소 , 업데이트)
    //4. 예약 연동 재고조회 (1.예약 생성 시 재고 감소 2. 예약 취소 시 재고 복구)
    //5. 재고가 0이면 자동 품절 처리





}
