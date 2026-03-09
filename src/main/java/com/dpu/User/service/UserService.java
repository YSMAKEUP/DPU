package com.dpu.User.service;

import com.dpu.User.domain.User;
import com.dpu.User.dto.*;
import com.dpu.User.repository.UserRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserService {
    private  final  UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    //회원가입
    @Transactional
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
        user.setRole(requestDto.getRole());



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
    @Transactional
    public LoginResponseDto login(LoginRequestDto requestDto) {
        User user = userRepository.findByEmail(requestDto.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않은 이메일입니다."));

        if (!passwordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        return LoginResponseDto.builder()
                .result("success")
                .userId(user.getId())
                .role(user.getRole())
                .name(user.getName())   // 추가
                .email(user.getEmail()) // 추가
                .build();
    }
    //단건조회
    @Transactional(readOnly = true)
    public User findById(Long userId){
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));
    }

    @Transactional
    public void updateUser(Long id, UserUpdateDto dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("회원 없음"));

        user.setName(dto.getName());
        user.setEmail(dto.getEmail());

        if (dto.getNewPassword() != null && !dto.getNewPassword().isBlank()) {
            if (!dto.getNewPassword().equals(dto.getConfirmPassword())) {
                throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
            }
            user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        }
        userRepository.save(user);

    }


    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }
}
