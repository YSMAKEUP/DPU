package com.dpu;

import com.dpu.Product.dto.ProductDto;
import com.dpu.Product.repository.ProductRepository;
import com.dpu.Product.service.ProductService;
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
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class ProductServiceIntegrationTest {

    @Autowired private ProductService productService;
    @Autowired private ProductRepository productRepository;
    @Autowired private StoreRepository storeRepository;
    @Autowired private UserRepository userRepository;

    private Long storeId;

    @BeforeEach
    void setUp() {
        // 유저 생성
        User owner = new User();
        owner.setName("사장님");
        owner.setEmail("owner@test.com");
        owner.setPassword("password123");
        owner.setRole(Role.USER);
        userRepository.save(owner);

        // 가게 생성
        Store store = new Store();
        store.setName("테스트 가게");
        store.setAddress("서울시 테스트구");
        store.setUser(owner);
        store.setLatitude(37.5);
        store.setLongitude(127.0);
        store.setOpenTime(LocalTime.of(9, 0));
        store.setCloseTime(LocalTime.of(21, 0));
        storeRepository.save(store);
        storeId = store.getId();
    }

    @Test
    @DisplayName("성공: 상품 등록")
    void createProduct_Success() {
        // when
        Long productId = productService.createProduct(storeId, "마카롱", 3000, 10, false);

        // then
        assertThat(productId).isNotNull();
        assertThat(productRepository.findById(productId)).isPresent();
    }

    @Test
    @DisplayName("성공: 상품 단건 조회")
    void findProductDto_Success() {
        // given
        Long productId = productService.createProduct(storeId, "마카롱", 3000, 10, false);

        // when
        ProductDto response = productService.findProductDto(productId);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getProductName()).isEqualTo("마카롱");
        assertThat(response.getPrice()).isEqualTo(3000);
    }

    @Test
    @DisplayName("성공: 상품 수정")
    void updateProduct_Success() {
        // given
        Long productId = productService.createProduct(storeId, "마카롱", 3000, 10, false);

        // when
        productService.updateProduct(productId, storeId, 5000, 20, false);

        // then
        ProductDto response = productService.findProductDto(productId);
        assertThat(response.getPrice()).isEqualTo(5000);
        assertThat(response.getStockQuantity()).isEqualTo(20);
    }

    @Test
    @DisplayName("성공: 상품 삭제")
    void deleteProduct_Success() {
        // given
        Long productId = productService.createProduct(storeId, "마카롱", 3000, 10, false);

        // when
        productService.deleteProduct(productId);

        // then
        assertThat(productRepository.findById(productId)).isEmpty();
    }

    @Test
    @DisplayName("성공: 재고 0일 때 품절 처리")
    void decreaseStock_SoldOut() {
        // given
        Long productId = productService.createProduct(storeId, "마카롱", 3000, 5, false);

        // when
        productService.decreaseStock(productId, 5);

        // then
        assertThat(productRepository.findById(productId).orElseThrow().isSoldOut()).isTrue();
        assertThat(productRepository.findById(productId).orElseThrow().getQuantity()).isEqualTo(0);
    }

    @Test
    @DisplayName("실패: 재고 부족 시 예외 발생")
    void decreaseStock_InsufficientStock_Fail() {
        // given
        Long productId = productService.createProduct(storeId, "마카롱", 3000, 3, false);

        // when & then
        assertThatThrownBy(() -> productService.decreaseStock(productId, 5))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("재고가 부족합니다.");
    }
}