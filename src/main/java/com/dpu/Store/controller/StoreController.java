package com.dpu.Store.controller;

import com.dpu.Store.dto.StoreResponseDto;
import com.dpu.Store.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stores")
@RequiredArgsConstructor
public class StoreController {

    private final StoreService storeService;

    // 주변 가게 조회 (위도/경도 기반)
    @GetMapping
    public ResponseEntity<List<StoreResponseDto>> getNearbyStores(
            @RequestParam double latitude,
            @RequestParam double longitude) {
        // TODO: 거리 계산 로직 구현 필요
        return ResponseEntity.ok().build();
    }

    // 가게 이름으로 검색
    @GetMapping("/search")
    public ResponseEntity<List<StoreResponseDto>> searchStores(
            @RequestParam String name) {
        // storeService.getStore(name) 사용
        return ResponseEntity.ok().build();
    }

    // 특정 가게 조회
    @GetMapping("/{storeId}")
    public ResponseEntity<StoreResponseDto> getStoreDetail(@PathVariable Long storeId) {
        // TODO: storeService에 findById 메서드 추가 필요
        return ResponseEntity.ok().build();
    }

    // 가게 영업 여부 확인
    @GetMapping("/{storeId}/business-status")
    public ResponseEntity<Boolean> isBusinessDay(@PathVariable Long storeId) {
        boolean isOpen = storeService.isBusinessDay(storeId);
        return ResponseEntity.ok(isOpen);
    }

    // 픽업 가능 여부 확인
    @GetMapping("/{storeId}/pickup-available")
    public ResponseEntity<Boolean> isPickupAvailable(@PathVariable Long storeId) {
        boolean canPickup = storeService.isPickup(storeId);
        return ResponseEntity.ok(canPickup);
    }
}