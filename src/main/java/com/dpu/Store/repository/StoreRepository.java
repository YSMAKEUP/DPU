package com.dpu.Store.repository;

import com.dpu.Store.domain.Store;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StoreRepository extends JpaRepository<Store,Long> {

        // 1) 사장(OWNER)의 내 가게 목록
        @EntityGraph(attributePaths = {"products"})
        List<Store> findByOwner_Id(Long ownerId);

        // 2) 특정 사장의 특정 가게 조회 (권한 검증용)
        Optional<Store> findByIdAndOwnerId(Long storeId, Long ownerId);

        // 3) 가게 이름으로 검색 (부분 검색)
        List<Store> findByNameContaining(String name);

        // 4) 가게 존재 여부(권한 검증 전에 가볍게 체크하고 싶을 때)
        boolean existsByIdAndOwnerId(Long id, Long ownerId);

        // 5) 가게 이름 존재 여부 (등록/수정 시 중복 체크)
        boolean existsByName(String name);

        // 거리 기반 검색 (Haversine 공식 사용 + 인덱스 최적화)
        @Query(value = "SELECT * FROM stores WHERE " +
                "latitude BETWEEN :latitude - :latRange AND :latitude + :latRange " +
                "AND longitude BETWEEN :longitude - :lngRange AND :longitude + :lngRange " +
                "AND (6371 * acos(cos(radians(:latitude)) * cos(radians(latitude)) * " +
                "cos(radians(longitude) - radians(:longitude)) + " +
                "sin(radians(:latitude)) * sin(radians(latitude)))) < :radius",
                nativeQuery = true)
        List<Store> findNearbyStores(@Param("latitude") double latitude,
                                     @Param("longitude") double longitude,
                                     @Param("radius") double radius,
                                     @Param("latRange") double latRange,
                                     @Param("lngRange") double lngRange);
}