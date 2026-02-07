package com.dpu.Product.service;
import com.dpu.Store.domain.Store;
import com.dpu.Store.repository.StoreRepository;
import com.dpu.User.domain.Role;
import com.dpu.Product.domain.Product;
import com.dpu.Product.repository.ProductRepository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class ProductService {
    private  final ProductRepository productRepository;
    private  final StoreRepository StoreRepository;

    public ProductService(ProductRepository productRepository, StoreRepository StoreRepository){
        this.productRepository = productRepository;
        this.StoreRepository = StoreRepository;
    }
    //1. 상품 목록 조회(가게별, 특정 메뉴별), 상품 상세 조회
    public List<Product> findStore(Long storeId){
        return productRepository.findByStoreId(storeId);
    }

    //1-2 특정 메뉴별
    public Optional<Product> findProduct(Long id){
        return productRepository.findById(id);
    }



    //2. 상품 관리( 1.등록 2.수정 3. 삭제)
    //2-1 등록 일 단 상품이 살아있는지 없는지에 대해서 조회를 해야한다.

    public Long creatProduct(Long id, String name, int price ,int quantity ,boolean soldOut, LocalDateTime createdAt){
        if (productRepository.existsById(id))
            throw new IllegalArgumentException("이미 등록이 완료된 메뉴입니다.");

        Product product = new Product();
        product.setId(id);
        product.setName(name);
        product.setPrice(price);
        product.setQuantity(quantity);
        product.setSoldOut(soldOut);
        product.setCreatedAt(createdAt);

        productRepository.save(product);
        return  product.getId();
    }

    //2-2 수정
    @Transactional
    public Long updateProduct(Long id, Long storeId,int price ,int quantity ,boolean soldOut){
        //1. 가게가 먼저 있는지 2. 가게에 상품이 잇는지 조회
        Store store = StoreRepository.findById(storeId).orElseThrow(() -> new IllegalArgumentException("가게가 없거나 사장 권한이 없습니다."));

        Product product = productRepository.findByIdAndStoreId(id, storeId).orElseThrow(() -> new IllegalArgumentException("가게에 존재하지 않은 상품입니다."));


        //수정
        product.setPrice(price);
        product.setQuantity(quantity);
        product.setSoldOut(soldOut);
        productRepository.save(product);
        return product.getId();


    }

    //삭제
    public void deleteProduct(Product product){
        this.productRepository.delete(product);

    }
//---------------------------------------------------------
    //사장님 관련 재고
    //3. 재고 조회 ,재고 수동 조정(증가 , 감소 , 업데이트)
public List<Product> findProductsByStore(Long storeId) {
    return productRepository.findByStoreId(storeId);
}

//4. 예약 연동 재고조회 (1.예약 생성 시 재고 감소 2. 예약 취소 시 재고 복구)
    //여기서 count은 고객의 상품을 구매한 갯수
@Transactional
public void findReservation(Long productId, int count) {
    Product product = productRepository.findById(productId).orElseThrow(() -> new IllegalArgumentException("상품이 없습니다."));

    if (product.getQuantity() < count) {
        throw new IllegalArgumentException("재고가 부족합니다.");

    }
    int quantity = product.getQuantity() - count;
    product.setQuantity(quantity);

//5. 재고가 0이면 자동 품절 처리
    if (product.getQuantity() == 0) {
        throw new IllegalArgumentException("품절 되었습니다.");
    }
}
@Transactional
public void canCelReservation(Long productId, int count){
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("상품이 없습니다."));

        product.setQuantity(product.getQuantity() + count);

    if (product.getQuantity() > 0) {
        product.setSoldOut(false);
    }



}


}
