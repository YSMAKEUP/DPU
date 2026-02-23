package com.dpu.Store.service;

import com.dpu.Product.domain.Product;
import com.dpu.Product.repository.ProductRepository;
import com.dpu.Store.dto.StoreCreateRequestDto;
import com.dpu.Store.dto.StoreResponseDto;
import com.dpu.Store.repository.StoreRepository;
import com.dpu.Store.domain.Store;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StoreService {

    private final StoreRepository storeRepository;
    private final ProductRepository productRepository;

    // 가게 등록
    @Transactional
    public Long createStore(StoreCreateRequestDto requestDto) {
        if (storeRepository.existsByName(requestDto.getName())) {
            throw new IllegalArgumentException("이미 존재하는 가게 이름입니다.");
        }

        Store store = new Store();
        store.setName(requestDto.getName());
        store.setAddress(requestDto.getAddress());
        store.setLongitude(requestDto.getLongitude());
        store.setLatitude(requestDto.getLatitude());
        store.setKakaoPlaceId(requestDto.getKakaoPlaceId());
        store.setOpenTime(requestDto.getOpenTime());
        store.setCloseTime(requestDto.getCloseTime());
        store.setClosedDay(requestDto.getClosedDay());
        store.setPickupCutoffMinutes(requestDto.getPickupCutoffMinutes());

        storeRepository.save(store);
        return store.getId();
    }

    // 가게 수정
    @Transactional
    public void updateStore(Long storeId, StoreCreateRequestDto requestDto) {
        Store store = findById(storeId);
        store.setName(requestDto.getName());
        store.setAddress(requestDto.getAddress());
        store.setLongitude(requestDto.getLongitude());
        store.setLatitude(requestDto.getLatitude());
        store.setOpenTime(requestDto.getOpenTime());
        store.setCloseTime(requestDto.getCloseTime());
        store.setClosedDay(requestDto.getClosedDay());
        store.setPickupCutoffMinutes(requestDto.getPickupCutoffMinutes());
    }

    // 가게 삭제
    @Transactional
    public void deleteStore(Long storeId) {
        Store store = findById(storeId);
        storeRepository.delete(store);
    }

    // 전체 가게 조회
    public List<StoreResponseDto> getAllStores() {
        return storeRepository.findAll().stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    // 특정 가게 이름 검색
    public List<StoreResponseDto> getStore(String name) {
        return storeRepository.findByNameContaining(name).stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    // 가게 상세 조회
    public Store findById(Long storeId) {
        return storeRepository.findById(storeId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 가게입니다."));
    }

    // 주변 가게 조회
    public List<StoreResponseDto> getNearbyStores(double latitude, double longitude, double radius) {
        return storeRepository.findNearbyStores(latitude, longitude, radius).stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    // 영업일 확인
    public boolean isBusinessDay(Long storeId) {
        Store store = findById(storeId);
        Integer closedDay = store.getClosedDay();

        if (closedDay == null) return true;

        int today = LocalDate.now().getDayOfWeek().getValue();
        return today != closedDay;
    }

    // 픽업 가능 여부
    public boolean isPickup(Long storeId) {
        Store store = findById(storeId);

        if (!isBusinessDay(storeId)) return false;

        Integer pickUpOff = store.getPickupCutoffMinutes();
        int minutes = (pickUpOff == null ? 30 : pickUpOff);

        LocalTime deadline = store.getCloseTime().minusMinutes(minutes);
        return LocalTime.now().isBefore(deadline);
    }

    // Entity → DTO 변환
    public StoreResponseDto toResponseDto(Store store) {
        List<Product> products = productRepository.findByStoreId(store.getId());
        return new StoreResponseDto(
                store.getId(),
                store.getName(),
                products,
                (long) store.getLatitude(),
                (long) store.getLongitude()
        );
    }
}