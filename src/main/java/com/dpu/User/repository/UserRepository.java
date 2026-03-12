package com.dpu.User.repository;

import com.dpu.User.domain.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User,Long> {


    //회원 존재 여부(이메일로 찾기)
    boolean existsByEmail(String email);

    Optional<User>findByEmail(String email);

    void deleteById(Long userId);
}
