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
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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
        // when
        Long storeId = storeService.createStore(storeRequest, ownerId);

        // then
        assertThat(storeId).isNotNull();
        assertThat(storeRepository.findById(storeId)).isPresent();
    }

    @Test
    @DisplayName("성공: 가게 단건 조회")
    void getStoreById_Success() {
        // given
        Long storeId = storeService.createStore(storeRequest, ownerId);

        // when
        StoreResponseDto response = storeService.getStoreById(storeId);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getStoreName()).isEqualTo("테스트 가게");
        assertThat(response.getAddress()).isEqualTo("서울시 테스트구");
    }

    @Test
    @DisplayName("성공: 가게 수정")
    void updateStore_Success() {
        // given
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

        // when
        storeService.updateStore(storeId, updateRequest);

        // then
        StoreResponseDto response = storeService.getStoreById(storeId);
        assertThat(response.getStoreName()).isEqualTo("수정된 가게");
        assertThat(response.getAddress()).isEqualTo("서울시 수정구");
    }

    @Test
    @DisplayName("성공: 가게 삭제")
    void deleteStore_Success() {
        // given
        Long storeId = storeService.createStore(storeRequest, ownerId);

        // when
        storeService.deleteStore(storeId);

        // then
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

        // when
        List<StoreResponseDto> stores = storeService.getAllStores();

        // then
        assertThat(stores).hasSize(2);
    }
}