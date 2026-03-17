package com.dpu;
import com.dpu.Product.domain.Product;
import com.dpu.Product.repository.ProductRepository;
import com.dpu.Reservation.dto.OrderItemDto;
import com.dpu.Reservation.dto.ReservationRequestDto;
import com.dpu.Reservation.dto.ReservationResponseDto;
import com.dpu.Reservation.service.ReservationService;
import com.dpu.Store.domain.Store;
import com.dpu.Store.repository.StoreRepository;
import com.dpu.User.domain.Role;
import com.dpu.User.domain.User;
import com.dpu.User.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ReservationServiceIntegrationTest {

    @Autowired private ReservationService reservationService;
    @Autowired private ProductRepository productRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private StoreRepository storeRepository;

    private Long savedProductId;

    @BeforeEach
    void setUp() {
        // 1. 유저 저장
        User user = new User();
        user.setName("테스터");
        user.setEmail("test@test.com");
        user.setPassword("1234");
        user.setRole(Role.USER); // Role enum 값 확인 필요
        userRepository.save(user);

        // 2. SecurityContext에 유저 세팅 (getCurrentUser()가 email로 조회함)
        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken("test@test.com", null, List.of());
        SecurityContextHolder.getContext().setAuthentication(auth);

        // 3. Store 저장 (owner 필수)
        Store store = new Store();
        store.setName("테스트 가게");
        store.setAddress("서울시 테스트구");
        store.setUser(user);           // owner 연결
        store.setLatitude(37.5);
        store.setLongitude(127.0);
        store.setOpenTime(LocalTime.of(9, 0));
        store.setCloseTime(LocalTime.of(21, 0));
        storeRepository.save(store);

        // 4. Product 저장 (store 연결 필수)
        Product product = new Product();
        product.setName("마카롱");
        product.setPrice(3000);
        product.setQuantity(10);
        product.setSoldOut(false);
        product.setStore(store);       // store 연결
        productRepository.save(product);
        savedProductId = product.getId();
    }

    @Test
    @DisplayName("성공: 예약 시 재고 감소")
    void createReservation_Success() {
        // given
        OrderItemDto orderItemDto = new OrderItemDto(savedProductId, 3, 3000);

        ReservationRequestDto request = new ReservationRequestDto();
        request.setPickTime(LocalDateTime.now().plusDays(1));
        request.setOrderItems(List.of(orderItemDto));

        // when
        ReservationResponseDto response = reservationService.createReservation(request);

        // then
        assertThat(response).isNotNull();

        Product updatedProduct = productRepository.findById(savedProductId).orElseThrow();
        assertThat(updatedProduct.getQuantity()).isEqualTo(7); // 10 - 3
    }



    @Test
    @DisplayName("성공: 예약 취소 시 재고 복구")
    void deleteReservation_RestoresStock() {
        // given
        OrderItemDto orderItemDto = new OrderItemDto(savedProductId, 3, 3000);

        ReservationRequestDto request = new ReservationRequestDto();
        request.setPickTime(LocalDateTime.now().plusDays(1));
        request.setOrderItems(List.of(orderItemDto));

        ReservationResponseDto response = reservationService.createReservation(request);
        Long reservationId = response.getReservationId(); // getter명 확인 필요

        // when
        reservationService.deleteReservation(reservationId);


        // then
        Product updatedProduct = productRepository.findById(savedProductId).orElseThrow();
        assertThat(updatedProduct.getQuantity()).isEqualTo(10); // 원복
    }
}