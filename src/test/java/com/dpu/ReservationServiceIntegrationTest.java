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
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class ReservationServiceIntegrationTest {

    @Autowired private ReservationService reservationService;
    @Autowired private ProductRepository productRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private StoreRepository storeRepository;
    @Autowired private EntityManager em;

    private Long savedProductId;

    @BeforeEach
    void setUp() {
        // ✅ deleteAll() 제거 - @Transactional 롤백으로 충분하므로 불필요

        // 테스트 유저 생성
        User user = new User();
        user.setName("테스터");
        user.setEmail("test@test.com");
        user.setPassword("1234");
        user.setRole(Role.USER);
        userRepository.save(user);

        // SecurityContext 설정
        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken("test@test.com", null, List.of());
        SecurityContextHolder.getContext().setAuthentication(auth);

        // 테스트 가게 생성
        Store store = new Store();
        store.setName("테스트 가게");
        store.setAddress("서울시 테스트구");
        store.setUser(user);
        store.setLatitude(37.5);
        store.setLongitude(127.0);
        store.setOpenTime(LocalTime.of(9, 0));
        store.setCloseTime(LocalTime.of(21, 0));
        storeRepository.save(store);

        // 테스트 상품 생성
        Product product = new Product();
        product.setName("마카롱");
        product.setPrice(3000);
        product.setQuantity(10);
        product.setSoldOut(false);
        product.setStore(store);
        productRepository.save(product);

        savedProductId = product.getId();

        // 영속성 컨텍스트 초기화 (LAZY 로딩 프록시 문제 방지)
        em.flush();
        em.clear();
    }

    @Test
    @DisplayName("성공: 예약 시 재고 감소")
    void createReservation_Success() {
        OrderItemDto orderItemDto = new OrderItemDto(savedProductId, 3, 3000);
        ReservationRequestDto request = new ReservationRequestDto();
        request.setPickTime(LocalDateTime.now().plusDays(1));
        request.setOrderItems(List.of(orderItemDto));

        ReservationResponseDto response = reservationService.createReservation(request);

        assertThat(response).isNotNull();

        // 재고 감소 확인 (10 - 3 = 7)
        Product updatedProduct = productRepository.findById(savedProductId).orElseThrow();
        assertThat(updatedProduct.getQuantity()).isEqualTo(7);
    }

    @Test
    @DisplayName("성공: 예약 취소 시 재고 복구")
    void deleteReservation_RestoresStock() {
        // 1. 예약 생성
        OrderItemDto orderItemDto = new OrderItemDto(savedProductId, 3, 3000);
        ReservationRequestDto request = new ReservationRequestDto();
        request.setPickTime(LocalDateTime.now().plusDays(1));
        request.setOrderItems(List.of(orderItemDto));

        ReservationResponseDto response = reservationService.createReservation(request);
        Long reservationId = response.getReservationId();

        // 2. DB 반영 후 1차 캐시 비우기
        em.flush();
        em.clear();

        // 3. 예약 취소
        reservationService.deleteReservation(reservationId);

        // 4. 재고 복구 확인 (10 - 3 + 3 = 10)
        Product updatedProduct = productRepository.findById(savedProductId).orElseThrow();
        assertThat(updatedProduct.getQuantity()).isEqualTo(10);
    }
}