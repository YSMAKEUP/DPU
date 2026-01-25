package com.dpu.User.service;

import com.dpu.User.domain.User;
import com.dpu.User.repository.UserRepository;
import org.springframework.stereotype.Service;

//
//회원가입
//
//로그인 / 로그아웃
//
//권한 부여 (USER / OWNER)
//
//이메일 중복 체크
//
//비밀번호 암호화
//
//내 정보 조회 / 수정
//
//회원 탈퇴(비활성 처리)
@Service
public class userService {
    private  final  UserRepository userRepository;

    //생성자 만들기
    public userService (UserRepository userRepository){
        this.userRepository = userRepository;
    }

    //회원가입
    public String signUp(String email,String name,String password){

        //검증 먼저 필요 왜? 불필요한 객체 생성을 막기 위해서
        if (userRepository.existsByEmail(email)){
           throw new IllegalArgumentException("존재하는 이메일입니다.");
        }
        //get 아닌 set를 사용해야
        User user = new User();
        user.setEmail(email);
        user.setName(name);
        user.setPassword(password);
        userRepository.save(user);
        return "가입이 완료되었습니다.";

    }




}
