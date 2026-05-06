package com.dpu.User.service;

import com.dpu.User.domain.User;
import com.dpu.User.dto.*;
import com.dpu.User.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // 회원가입
    @Transactional
    public SignUpResponseDto signUp(SignUpRequestDto requestDto) {
        if (userRepository.existsByEmail(requestDto.getEmail())) {
            throw new IllegalArgumentException("존재하는 이메일입니다.");
        }

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

    // 로그인 (기존 유지 - 혹시 다른 곳에서 사용할 수도 있으므로)
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
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

    // 단건 조회 (ID)
    @Transactional(readOnly = true)
    public User findById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));
    }

    // ✅ 단건 조회 (이메일) - Principal.getName() 으로 조회할 때 사용
    @Transactional(readOnly = true)
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));
    }

    // 회원정보 수정
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

    // 회원 탈퇴
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }
}