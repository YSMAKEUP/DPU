package com.dpu.global.dto;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ErrorResponse {

    //response에 받기 위한 변수 생성
    private final int status;
    private final String message;
    private final LocalDateTime timestamp;

    public ErrorResponse(int status , String message){
        this.status = status;
        this.message = message;
        this.timestamp =LocalDateTime.now();
    }


}
