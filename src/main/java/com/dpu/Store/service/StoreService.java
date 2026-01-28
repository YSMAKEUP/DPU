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

import java.util.List;

public class StoreService {
    private  final StoreRepository storeRepository;

   public StoreService (StoreRepository storeRepository){
       this.storeRepository = storeRepository;
   }
   //가게 등록(회원 등록) --> 먼저 검증 -->없다면 추가.
   //1. 회원 등록 2. 수정 3. 삭제
   public Long createStore(String name, String address, double longitude, double latitude,long kakaoPlaceId) {
       if (storeRepository.existsByName(name)) {
           throw new IllegalArgumentException("이미 존재하는 가게 이름입니다.");
       }

       Store store = new Store(); //객체를 생성
       store.setName(name);
       store.setAddress(address);
       store.setLongitude(longitude);
       store.setLatitude(latitude);
       store.setKakaoplaceID(kakaoPlaceId);
       storeRepository.save(store);
       return store.getId();

   }

   //2.가게 조회 (디저트 메뉴에 대한 조회), 특정 가게 조회 ,디저트 특정 메뉴 조회
    //특정 가게 조회는 값을 미미제과점 ==인데 미미가 뜨거나 그런거? 특정 키워드가 필터링 되도록 설정

    //1.특정 가게 조회
    public List<Store> getStore(String name){

       return storeRepository.findByStoreNameContaining(name);

    }

   //3.영업일 /휴무일에 대한 조회




   //4.영업 상태/ 픽업 마감 계산


}
