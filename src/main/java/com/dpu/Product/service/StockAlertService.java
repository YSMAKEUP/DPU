package com.dpu.Product.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class StockAlertService {

    @Async
    public void sendLowStockAlert(String productName, int remainingStock) {
        // 실제 알람 발송 로직 (이메일, 슬랙 등)
        // 지금은 로그로 대체
        log.info("[비동기 재고 알람] 상품명: {}, 남은 재고: {}개 - 재고가 부족합니다!",
                productName, remainingStock);
    }
}