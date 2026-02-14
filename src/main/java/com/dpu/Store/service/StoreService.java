package com.dpu.Store.service;

//가게 조회 (리스트 / 상세)
//
//디저트 종류 / 가게 조회
//
//영업 / 휴무 (MVP 1단계)
//
//요일별 영업시간(요일 스케줄)
//
//특정 날짜 휴무(예외 휴무)
//
//픽업 마감: 영업 종료 30분 전
//
//브레이크 타임 없음
//
//가게 관리(OWNER)
//
//가게 등록 / 수정 / 삭제
//
//가게 상세 조회 시 메뉴(상품) 목록 함께 제공
import com.dpu.Store.repository.StoreRepository;
import com.dpu.Store.domain.Store;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class StoreService {
    private  final StoreRepository storeRepository;

   public StoreService (StoreRepository storeRepository){
       this.storeRepository = storeRepository;
   }
   //가게 등록(회원 등록) --> 먼저 검증 -->없다면 추가.
   //1. 회원 등록 2. 수정 3. 삭제
   public Long createStore(String name, String address, double longitude, double latitude, long kakaoPlaceId, LocalTime openTime, LocalTime closeTime , Integer closedDay) {
       if (storeRepository.existsByName(name)) {
           throw new IllegalArgumentException("이미 존재하는 가게 이름입니다.");
       }

       Store store = new Store(); //객체를 생성
       store.setName(name);
       store.setAddress(address);
       store.setLongitude(longitude);
       store.setLatitude(latitude);
       store.setKakaoPlaceId(kakaoPlaceId);
       store.setOpenTime(openTime);
       store.setCloseTime(closeTime);
       store.setClosedDay(closedDay);
       storeRepository.save(store);
       return store.getId();

   }

   //2.가게 조회 (디저트 메뉴에 대한 조회), 특정 가게 조회 ,디저트 특정 메뉴 조회
    //특정 가게 조회는 값을 미미제과점 == 인데 미미가 뜨거나 그런거? 특정 키워드가 필터링 되도록 설정

    //1.특정 가게 조회
    public List<Store> getStore(String name){

       return storeRepository.findByNameContaining(name);
    }


   //3.영업일 /휴무일에 대한 조회

    public boolean isBusinessDay(Long storeId){
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new IllegalArgumentException("등록되지 않은 가게입니다."));

        Integer closedDay = store.getClosedDay();
        if (closedDay == null)
            return true;

        int today = LocalDate.now().getDayOfWeek().getValue(); // 1~7
        return today != closedDay;

   }

   //픽업 마감 판단
    public boolean isPickup(Long storeId){
       Store store = storeRepository.findById(storeId)
            .orElseThrow(() -> new IllegalArgumentException("등록되지 않은 가게입니다."));


       if (!isBusinessDay(storeId)) return false;
//
       Integer pickUpOff = store.getPickupCutoffMinutes();
       int minutes =  (pickUpOff == null ? 30 : pickUpOff);

       //픽업 마감 시간
        LocalTime deadline = store.getCloseTime().minusMinutes(minutes);
        return  LocalTime.now().isBefore(deadline);




   }



}
