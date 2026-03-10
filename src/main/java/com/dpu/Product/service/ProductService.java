package com.dpu.Product.service;
import com.dpu.Product.domain.Product;
import com.dpu.Product.dto.ProductDto;
import com.dpu.Product.dto.ProductResponseDto;
import com.dpu.Product.repository.ProductRepository;
import com.dpu.Store.domain.Store;
import com.dpu.Store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final StoreRepository storeRepository;


    // 가게별 상품 목록 조회 (DTO 반환)
    public ProductResponseDto getProductsByStore(Long storeId) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 가게입니다."));

        List<ProductDto> productDtos = productRepository.findByStoreId(storeId).stream()
                .map(p -> new ProductDto(p.getId(), p.getStore().getId(), p.getName(), p.getPrice(), p.getStockQuantity(), p.isSoldOut(), p.getCreatedAt()))
                .collect(Collectors.toList());

        return new ProductResponseDto(storeId, store.getName(), productDtos);
    }

    // 특정 상품 단건 조회 (DTO 반환)
    public ProductDto findProductDto(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품입니다."));

        return new ProductDto(
                product.getId(),
                product.getStore().getId(),
                product.getName(),
                product.getPrice(),
                product.getStockQuantity(),
                product.isSoldOut(),
                product.getCreatedAt()
        );
    }

    // 상품 등록
    @Transactional
    public Long createProduct(Long storeId, String name, int price, int quantity, boolean soldOut) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 가게입니다."));

        Product product = new Product();
        product.setStore(store);
        product.setName(name);
        product.setPrice(price);
        product.setQuantity(quantity);
        product.setSoldOut(soldOut);

        return productRepository.save(product).getId();
    }

    // 상품 수정
    @Transactional
    public Long updateProduct(Long id, Long storeId, int price, int quantity, boolean soldOut) {
        storeRepository.findById(storeId)
                .orElseThrow(() -> new IllegalArgumentException("가게가 없거나 사장 권한이 없습니다."));

        Product product = productRepository.findByIdAndStoreId(id, storeId)
                .orElseThrow(() -> new IllegalArgumentException("가게에 존재하지 않는 상품입니다."));

        product.setPrice(price);
        product.setQuantity(quantity);
        product.setSoldOut(soldOut);

        return product.getId();
    }

    // 상품 삭제
    @Transactional
    public Long deleteProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품입니다."));

        Long storeId = product.getStore().getId();
        productRepository.delete(product);
        return storeId;
    }

    // 예약 생성 시 재고 감소
    @Transactional
    public void decreaseStock(Long productId, int count) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("상품이 없습니다."));

        if (product.getQuantity() < count) {
            throw new IllegalArgumentException("재고가 부족합니다.");
        }

        product.setQuantity(product.getQuantity() - count);

        if (product.getQuantity() == 0) {
            product.setSoldOut(true);
        }
    }

    // 예약 취소 시 재고 복구
    @Transactional
    public void restoreStock(Long productId, int count) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("상품이 없습니다."));

        product.setQuantity(product.getQuantity() + count);
        product.setSoldOut(false);
    }
}