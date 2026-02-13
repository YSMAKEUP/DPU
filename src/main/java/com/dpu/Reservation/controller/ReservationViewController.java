package com.dpu.Reservation.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/reservation")
public class ReservationViewController {

    // 예약 페이지
    @GetMapping
    public String reservationPage() {
        return "reservation/reservation";
    }

    // 예약 상세 페이지
    @GetMapping("/{reservationId}")
    public String reservationDetailPage(@PathVariable Long reservationId, Model model) {
        model.addAttribute("reservationId", reservationId);
        return "reservation/reservation_detail";
    }
}