package com.dpu.User.repository;

import com.dpu.User.domain.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    //회원가입에서 creat,아이디를 찾아야 하고 회원 조회,수정,삭제
    //회원인지 아닌지 확인 - existByid
    //-회원 가입 저장 --email,password로 회원가입--save
    //findAll --> 모든 엔터티를 조회하여 리스트로 반환
    //회원 정보 삭제 --->deleteByid

    //회원 존재 여부(이메일로 찾기)
    boolean existsByEmail(String email);

    //회원 찾기
    Optional<User>findByEmail(String email);


    //이메일로 회원 삭제
    long deleteByEmail(String email);










}
