package com.dpu.Product.repository;

import com.dpu.Product.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository <Product,Long> {
    //디저트가 재고가 있는지 조회, 특정 디저트 재고 조회,디저트 상태 (품절), 메뉴 목록
    // 가게의 재고가 있는지 조회
    // 메뉴 목록 (매장별)

//    Long findById(Long id);
    
    List<Product> findByStoreId(Long storeId);

    // 판매중 메뉴 목록 (품절 제외)
    List<Product> findByStoreIdAndSoldOutFalse(Long storeId);

    // 재고 있는 메뉴 목록 (quantity > 0)
    List<Product> findByStoreIdAndQuantityGreaterThan(Long storeId, Long quantity);

    // 특정 매장의 특정 디저트 조회 (소속 검증/상세 조회)
    Optional<Product> findByIdAndStoreId(Long id, Long storeId);
}
