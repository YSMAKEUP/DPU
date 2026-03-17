package com.dpu;

import com.dpu.Product.domain.Product;
import com.dpu.Product.repository.ProductRepository;
import com.dpu.Reservation.dto.OrderItemDto;
import com.dpu.Reservation.dto.ReservationRequestDto;
import com.dpu.Store.domain.Store;
import com.dpu.Store.repository.StoreRepository;
import com.dpu.User.domain.Role;
import com.dpu.User.domain.User;
import com.dpu.User.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class AcceptanceTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private UserRepository userRepository;
    @Autowired private StoreRepository storeRepository;
    @Autowired private ProductRepository productRepository;

    private Long storeId;
    private Long productId;

    @BeforeEach
    void setUp() {
        // 유저 생성
        User user = new User();
        user.setName("테스터");
        user.setEmail("test@test.com");
        user.setPassword("1234");
        user.setRole(Role.USER);
        userRepository.save(user);

        // SecurityContext 세팅
        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken("test@test.com", null, List.of());
        SecurityContextHolder.getContext().setAuthentication(auth);

        // 가게 생성
        Store store = new Store();
        store.setName("테스트 가게");
        store.setAddress("서울시 테스트구");
        store.setUser(user);
        store.setLatitude(37.5);
        store.setLongitude(127.0);
        store.setOpenTime(LocalTime.of(9, 0));
        store.setCloseTime(LocalTime.of(21, 0));
        storeRepository.save(store);
        storeId = store.getId();

        // 상품 생성
        Product product = new Product();
        product.setName("마카롱");
        product.setPrice(3000);
        product.setQuantity(10);
        product.setSoldOut(false);
        product.setStore(store);
        productRepository.save(product);
        productId = product.getId();
    }

    // ===================== StoreController 인수테스트 =====================

    @Test
    @DisplayName("인수: 가게 단건 조회 API")
    void getStoreDetail() throws Exception {
        mockMvc.perform(get("/api/stores/" + storeId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.storeName").value("테스트 가게"))
                .andExpect(jsonPath("$.address").value("서울시 테스트구"));
    }

    @Test
    @DisplayName("인수: 가게 이름 검색 API")
    void searchStores() throws Exception {
        mockMvc.perform(get("/api/stores/search")
                        .param("name", "테스트"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].storeName").value("테스트 가게"));
    }

    @Test
    @DisplayName("인수: 주변 가게 조회 API")
    void getNearbyStores() throws Exception {
        mockMvc.perform(get("/api/stores")
                        .param("latitude", "37.5")
                        .param("longitude", "127.0")
                        .param("radius", "3.0"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("인수: 가게 영업 여부 확인 API")
    void isBusinessDay() throws Exception {
        mockMvc.perform(get("/api/stores/" + storeId + "/business-status"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("인수: 픽업 가능 여부 확인 API")
    void isPickupAvailable() throws Exception {
        mockMvc.perform(get("/api/stores/" + storeId + "/pickup-available"))
                .andExpect(status().isOk());
    }

    // ===================== ReservationController 인수테스트 =====================

    @Test
    @DisplayName("인수: 예약 생성 API")
    void createReservation() throws Exception {
        OrderItemDto orderItem = new OrderItemDto(productId, 2, 3000);
        ReservationRequestDto request = new ReservationRequestDto();
        request.setPickTime(LocalDateTime.now().plusDays(1));
        request.setOrderItems(List.of(orderItem));

        mockMvc.perform(post("/reservation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("인수: 예약 단건 조회 API")
    void getReservation() throws Exception {
        // given - 예약 먼저 생성
        OrderItemDto orderItem = new OrderItemDto(productId, 2, 3000);
        ReservationRequestDto request = new ReservationRequestDto();
        request.setPickTime(LocalDateTime.now().plusDays(1));
        request.setOrderItems(List.of(orderItem));

        String response = mockMvc.perform(post("/reservation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        Long reservationId = objectMapper.readTree(response).get("reservationId").asLong();

        // when & then
        mockMvc.perform(get("/reservation/" + reservationId))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("인수: 예약 취소 API")
    void deleteReservation() throws Exception {
        // given - 예약 먼저 생성
        OrderItemDto orderItem = new OrderItemDto(productId, 2, 3000);
        ReservationRequestDto request = new ReservationRequestDto();
        request.setPickTime(LocalDateTime.now().plusDays(1));
        request.setOrderItems(List.of(orderItem));

        String response = mockMvc.perform(post("/reservation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        Long reservationId = objectMapper.readTree(response).get("reservationId").asLong();

        // when & then
        mockMvc.perform(delete("/reservation/" + reservationId))
                .andExpect(status().isNoContent());
    }
}