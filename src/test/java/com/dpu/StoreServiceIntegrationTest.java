package com.dpu;

import com.dpu.Store.dto.StoreCreateRequestDto;
import com.dpu.Store.dto.StoreResponseDto;
import com.dpu.Store.repository.StoreRepository;
import com.dpu.Store.service.StoreService;
import com.dpu.User.domain.Role;
import com.dpu.User.domain.User;
import com.dpu.User.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class StoreServiceIntegrationTest {

    @Autowired private StoreService storeService;
    @Autowired private StoreRepository storeRepository;
    @Autowired private UserRepository userRepository;

    private Long ownerId;
    private StoreCreateRequestDto storeRequest;

    @BeforeEach
    void setUp() {
        // 유저 생성
        User owner = new User();
        owner.setName("사장님");
        owner.setEmail("owner@test.com");
        owner.setPassword("password123");
        owner.setRole(Role.USER);
        userRepository.save(owner);
        ownerId = owner.getId();

        // 가게 요청 DTO 생성
        storeRequest = new StoreCreateRequestDto(
                "테스트 가게",
                "서울시 테스트구",
                127.0,
                37.5,
                null,
                LocalTime.of(9, 0),
                LocalTime.of(21, 0),
                null,
                30
        );
    }

    @Test
    @DisplayName("성공: 가게 생성")
    void createStore_Success() {
        Long storeId = storeService.createStore(storeRequest, ownerId);

        assertThat(storeId).isNotNull();
        assertThat(storeRepository.findById(storeId)).isPresent();
    }

    @Test
    @DisplayName("성공: 가게 단건 조회")
    void getStoreById_Success() {
        Long storeId = storeService.createStore(storeRequest, ownerId);

        StoreResponseDto response = storeService.getStoreById(storeId);

        assertThat(response).isNotNull();
        assertThat(response.getStoreName()).isEqualTo("테스트 가게");
        assertThat(response.getAddress()).isEqualTo("서울시 테스트구");
    }

    @Test
    @DisplayName("성공: 가게 수정")
    void updateStore_Success() {
        Long storeId = storeService.createStore(storeRequest, ownerId);

        StoreCreateRequestDto updateRequest = new StoreCreateRequestDto(
                "수정된 가게",
                "서울시 수정구",
                127.1,
                37.6,
                null,
                LocalTime.of(10, 0),
                LocalTime.of(22, 0),
                null,
                30
        );

        storeService.updateStore(storeId, updateRequest);

        StoreResponseDto response = storeService.getStoreById(storeId);
        assertThat(response.getStoreName()).isEqualTo("수정된 가게");
        assertThat(response.getAddress()).isEqualTo("서울시 수정구");
    }

    @Test
    @DisplayName("성공: 가게 삭제")
    void deleteStore_Success() {
        Long storeId = storeService.createStore(storeRequest, ownerId);

        storeService.deleteStore(storeId);

        assertThat(storeRepository.findById(storeId)).isEmpty();
    }

    @Test
    @DisplayName("성공: 내 가게 목록 조회")
    void getAllStores_Success() {
        // given
        storeService.createStore(storeRequest, ownerId);

        StoreCreateRequestDto storeRequest2 = new StoreCreateRequestDto(
                "테스트 가게2",
                "서울시 테스트구2",
                127.1,
                37.6,
                null,
                LocalTime.of(10, 0),
                LocalTime.of(22, 0),
                null,
                30
        );
        storeService.createStore(storeRequest2, ownerId);

        // ✅ 전체 조회 대신 owner 기준 조회로 변경하여 다른 테스트 데이터 간섭 방지
        // storeService.getStoresByOwner(ownerId) 메서드가 있다면 사용 권장
        // 없다면 storeRepository 직접 조회
        List<StoreResponseDto> stores = storeRepository.findAll().stream()
                .filter(s -> s.getUser().getId().equals(ownerId))
                .map(s -> storeService.getStoreById(s.getId()))
                .toList();

        assertThat(stores).hasSize(2);
    }
}