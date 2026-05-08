package com.dpu.Product.repository;

import com.dpu.Product.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface ProductRepository extends JpaRepository <Product,Long> {

    List<Product> findByStoreId(Long storeId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT p FROM Product p WHERE p.id = :id")
    Optional<Product> findByIdWithLock(@Param("id") Long id);

    // 판매중 메뉴 목록 (품절 제외)
    List<Product> findByStoreIdAndSoldOutFalse(Long storeId);

    // 재고 있는 메뉴 목록 (quantity > 0)
    List<Product> findByStoreIdAndQuantityGreaterThan(Long storeId, int quantity);

    // 특정 매장의 특정 디저트 조회 (소속 검증/상세 조회)
    Optional<Product> findByIdAndStoreId(Long id, Long storeId);


}
