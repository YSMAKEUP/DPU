package com.dpu.Store.repository;

import com.dpu.Store.domain.store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StoreRepository extends JpaRepository<store,Long> {

        // 1) 사장(OWNER)의 내 가게 목록
        List<store> findByOwnerId(Long ownerId);

        // 2) 특정 사장의 특정 가게 조회 (권한 검증용)
        Optional<store> findByIdAndOwnerId(Long storeId, Long ownerId);

        // 3) 가게 이름으로 검색 (부분 검색)
        List<store> findByStoreNameContaining(String keyword);

        // 4) 가게 존재 여부(권한 검증 전에 가볍게 체크하고 싶을 때)
        boolean existsByIdAndOwnerId(Long storeId, Long ownerId);



}
