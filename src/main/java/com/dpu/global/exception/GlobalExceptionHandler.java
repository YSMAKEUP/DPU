package com.dpu.global.exception;

import com.dpu.global.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
    public class GlobalExceptionHandler {

        // 2. 특정 사고(IllegalArgumentException)가 발생하면 이 메서드가 낚아챕니다.
        @ExceptionHandler(IllegalArgumentException.class)
        public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException e) {

            // 3. 우리가 만든 예쁜 봉투(ErrorResponse)에 에러 내용을 담습니다.
            ErrorResponse errorResponse = new ErrorResponse(
                    HttpStatus.BAD_REQUEST.value(), // 400 에러 숫자
                    e.getMessage()                  // 발생한 에러 메시지 내용
            );

            // 4. 손님(클라이언트)에게 정중하게 전달합니다.
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
}
