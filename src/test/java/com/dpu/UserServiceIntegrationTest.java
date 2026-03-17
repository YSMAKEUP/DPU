package com.dpu;

import com.dpu.User.domain.Role;
import com.dpu.User.dto.LoginRequestDto;
import com.dpu.User.dto.LoginResponseDto;
import com.dpu.User.dto.SignUpRequestDto;
import com.dpu.User.dto.SignUpResponseDto;
import com.dpu.User.repository.UserRepository;
import com.dpu.User.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class UserServiceIntegrationTest {

    @Autowired private UserService userService;
    @Autowired private UserRepository userRepository;

    private SignUpRequestDto signUpRequest;

    @BeforeEach
    void setUp() {
        signUpRequest = new SignUpRequestDto(
                "테스터",
                "test@test.com",
                "password123",
                Role.USER
        );
    }

    @Test
    @DisplayName("성공: 회원가입")
    void signUp_Success() {
        SignUpResponseDto response = userService.signUp(signUpRequest);
        assertThat(response).isNotNull();
        assertThat(response.getEmail()).isEqualTo("test@test.com");
        assertThat(response.getName()).isEqualTo("테스터");
    }

    @Test
    @DisplayName("실패: 중복 이메일 회원가입")
    void signUp_DuplicateEmail_Fail() {
        userService.signUp(signUpRequest);
        assertThatThrownBy(() -> userService.signUp(signUpRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하는 이메일입니다.");
    }

    @Test
    @DisplayName("성공: 로그인")
    void login_Success() {
        userService.signUp(signUpRequest);
        LoginRequestDto loginRequest = new LoginRequestDto("test@test.com", "password123");
        LoginResponseDto response = userService.login(loginRequest);
        assertThat(response.getResult()).isEqualTo("success");
        assertThat(response.getEmail()).isEqualTo("test@test.com");
    }

    @Test
    @DisplayName("실패: 존재하지 않는 이메일 로그인")
    void login_EmailNotFound_Fail() {
        LoginRequestDto loginRequest = new LoginRequestDto("notexist@test.com", "password123");
        assertThatThrownBy(() -> userService.login(loginRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않은 이메일입니다.");
    }

    @Test
    @DisplayName("실패: 비밀번호 틀렸을 때 로그인")
    void login_WrongPassword_Fail() {
        userService.signUp(signUpRequest);
        LoginRequestDto loginRequest = new LoginRequestDto("test@test.com", "wrongpassword");
        assertThatThrownBy(() -> userService.login(loginRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("비밀번호가 일치하지 않습니다.");
    }
}