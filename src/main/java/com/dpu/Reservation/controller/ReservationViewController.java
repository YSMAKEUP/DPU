package com.dpu.Reservation.controller;
import com.dpu.Product.service.ProductService;
import com.dpu.Product.service.ProductService;
import com.dpu.Reservation.service.ReservationService;
import com.dpu.User.dto.LoginResponseDto;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/reservation")
@RequiredArgsConstructor
public class ReservationViewController {

    private final ReservationService reservationService;
    private final ProductService productService;

    // 예약 페이지
    @GetMapping
    public String reservationPage(@RequestParam Long storeId, Model model) {
        model.addAttribute("storeInfo", productService.getProductsByStore(storeId));
        model.addAttribute("storeId", storeId);
        return "reservation";
    }

    // 내 예약 목록
    @GetMapping("/list")
    public String reservationList(HttpSession session, Model model) {
        LoginResponseDto loginUser = (LoginResponseDto) session.getAttribute("loginUser");
        if (loginUser == null) return "redirect:/login";

        model.addAttribute("reservations", reservationService.getMyReservations(loginUser.getUserId()));
        return "reservation_list";
    }

    // 예약 상세 페이지
    @GetMapping("/detail/{reservationId}")
    public String reservationDetailPage(@PathVariable Long reservationId, Model model) {
        model.addAttribute("reservationId", reservationId);
        return "reservation_detail";
    }
}