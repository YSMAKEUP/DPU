package com.dpu.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IntroViewController {

    // 인트로 페이지 (메인)
    @GetMapping("/")
    public String introPage() {
        return "intro";
    }
}