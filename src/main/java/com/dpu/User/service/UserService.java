package com.dpu.User.service;

import com.dpu.User.domain.User;
import com.dpu.User.dto.LoginRequestDto;
import com.dpu.User.dto.LoginResponseDto;
import com.dpu.User.dto.SignUpRequestDto;
import com.dpu.User.dto.SignUpResponseDto;
import com.dpu.User.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserService {
    private  final  UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    //회원가입
    public SignUpResponseDto signUp(SignUpRequestDto requestDto) {

        //검증 먼저 필요 왜? 불필요한 객체 생성을 막기 위해서
        if (userRepository.existsByEmail(requestDto.getEmail())) {
            throw new IllegalArgumentException("존재하는 이메일입니다.");
        }
        //get 아닌 set를 사용해야
        User user = new User();
        user.setEmail(requestDto.getEmail());
        user.setName(requestDto.getName());
        user.setPassword(passwordEncoder.encode(requestDto.getPassword()));
        user.setRole(requestDto.getUserType());


        User savedUser = userRepository.save(user);

        return new SignUpResponseDto(
                savedUser.getId(),
                savedUser.getEmail(),
                savedUser.getName(),
                savedUser.getRole(),
                LocalDateTime.now()

        );
    }

    //로그인
    public LoginResponseDto login(LoginRequestDto requestDto){
        User user = userRepository.findByEmail(requestDto.getEmail())
                .orElseThrow(()-> new IllegalArgumentException("존재하지 않은 이메일입니다."));

        if (!passwordEncoder.matches(requestDto.getPassword(), user.getPassword())){
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        return LoginResponseDto.builder()
                .result("success")
                .userId(user.getId())
                .userType(user.getRole())
                .build();
    }









}
