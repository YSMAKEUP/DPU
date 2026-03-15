package com.dpu.Store;

import com.dpu.Store.domain.Store;
import com.dpu.Store.repository.StoreRepository;
import com.dpu.User.domain.User;
import com.dpu.User.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;

import static com.dpu.User.domain.Role.OWNER;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class StoreIntegrationTest {

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private UserRepository userRepository; // User 저장을 위해 추가

    @Test
    void 테스트_시작_체크() {
        // 1. User 생성 시 필수 필드 모두 세팅
        User owner = new User();
        owner.setName("테스트 사장님");
        owner.setEmail("test@example.com"); // 이 부분이 빠져서 에러가 났습니다!
        owner.setPassword("1234");          // 보통 필수값인 경우가 많으니 추가
        owner.setRole(OWNER);             // 로그에 role 컬럼도 보이니 적절한 값 추가

        userRepository.save(owner); // 이제 정상적으로 저장될 겁니다.

        // 2. 매장 생성
        Store store = new Store();
        store.setName("테스트 매장");
        store.setAddress("서울시 강남구");
        store.setLatitude(37.5665);
        store.setLongitude(126.9780);
        store.setOpenTime(LocalTime.of(9, 0));
        store.setCloseTime(LocalTime.of(22, 0));
        store.setPickupCutoffMinutes(30);

        // 3. 연관관계 설정
        store.setUser(owner);

        // 4. 저장 및 검증
        Store savedStore = storeRepository.save(store);
        assertThat(savedStore.getId()).isNotNull();
        assertThat(savedStore.getUser().getName()).isEqualTo("테스트 사장님");
    }
}
