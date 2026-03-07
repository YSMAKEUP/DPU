package com.dpu.Store.service;

import com.dpu.Product.domain.Product;
import com.dpu.Product.dto.ProductDto;
import com.dpu.Product.repository.ProductRepository;
import com.dpu.Store.dto.StoreCreateRequestDto;
import com.dpu.Store.dto.StoreResponseDto;
import com.dpu.Store.repository.StoreRepository;
import com.dpu.Store.domain.Store;
import com.dpu.User.domain.User;
import com.dpu.User.repository.UserRepository;
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
    private final UserRepository userRepository; // 추가

    // 가게 등록
    @Transactional
    public Long createStore(StoreCreateRequestDto requestDto, Long ownerId) {
        if (storeRepository.existsByName(requestDto.getName())) {
            throw new IllegalArgumentException("이미 존재하는 가게 이름입니다.");
        }

        User owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        Store store = new Store();
        store.setUser(owner); // owner 설정 추가!
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

    public StoreResponseDto getStoreById(Long storeId) {
        Store store = findById(storeId);
        return toResponseDto(store);
    }

    // 특정 가게 검증 (권한 조회)
    public Store findByIdAndOwnerId(Long storeId, Long ownerId) {
        return storeRepository.findByIdAndOwnerId(storeId, ownerId)
                .orElseThrow(() -> new IllegalArgumentException("권한이 없습니다."));
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

    private boolean isStoreOpen(Store store) {
        if (store.getOpenTime() == null || store.getCloseTime() == null) return false;
        LocalTime now = LocalTime.now();
        return now.isAfter(store.getOpenTime()) && now.isBefore(store.getCloseTime());
    }

    // Entity → DTO 변환
    public StoreResponseDto toResponseDto(Store store) {
        List<Product> products = productRepository.findByStoreId(store.getId());

        List<ProductDto> productDtos = products.stream()
                .map(p -> new ProductDto(
                        p.getId(),
                        p.getStore().getId(),
                        p.getName(),
                        p.getPrice(),
                        p.getStockQuantity(),
                        p.isSoldOut(),
                        p.getCreatedAt()
                ))
                .collect(Collectors.toList());

        return new StoreResponseDto(
                store.getId(),
                store.getName(),
                productDtos,
                store.getLatitude(),
                store.getLongitude(),
                store.getAddress(),
                store.getOpenTime(),
                store.getCloseTime(),
                store.getClosedDay(),
                isStoreOpen(store)
        );
    }
}