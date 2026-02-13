package com.dpu.Store.controller;

import com.dpu.Store.dto.StoreRequestDto;
import com.dpu.Store.dto.StoreResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/stores")
public class StoreController {

    // 주변 가게 조회 (위도/경도 기반)
    @GetMapping
    public ResponseEntity<StoreResponseDto> getNearbyStores(
            @RequestParam double latitude,
            @RequestParam double longitude) {
        // TODO: storeService.getNearbyStores(latitude, longitude);
        return ResponseEntity.ok().build();
    }
}