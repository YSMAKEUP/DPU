//package com.dpu.controller;
//import com.dpu.Product.service.ProductService;
//import com.dpu.Store.service.StoreService;
//import jakarta.servlet.http.HttpSession;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//
//@Controller
//public class HomeController {
//
//    @Autowired
//    private ProductService productService;
//
//    @Autowired
//    private StoreService storeService;
//
//    /**
//     * 메인 페이지
//     *
//     * - 비로그인: 디저트 목록 + 소개
//     * - 로그인: 디저트 목록 + 환영 메시지
//     */
//    @GetMapping("/")
//    public String home(Model model, HttpSession session) {
//        // 디저트 목록 (전체 또는 인기 상품)
//        model.addAttribute("products", productService.productRepository);
//
//        // 로그인 여부 확인
//        Long userId = (Long) session.getAttribute("userId");
//        if (userId != null) {
//            model.addAttribute("isLoggedIn", true);
//            model.addAttribute("userName", session.getAttribute("userName"));
//        } else {
//            model.addAttribute("isLoggedIn", false);
//        }
//
//        return "index"; // templates/index.html
//    }
//}