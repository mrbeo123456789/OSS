//package com.oss.controller;
//import com.oss.service.VNPayService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import com.oss.model.Order;
//import com.oss.service.OrderService;
//import java.util.Map;
//
//@Controller
//public class PaymentController {
//    @Autowired
//    private VNPayService vnPayService;
//@Autowired
//private OrderService orderService;
//    @GetMapping("/initiate_payment")
//    public String initiatePayment(@RequestParam("orderId") Long orderId, Model model) {
//        // Giả sử bạn có một dịch vụ để lấy thông tin đơn hàng từ orderId
//        Order order = orderService.getOrderById(orderId);
//        String paymentUrl = vnPayService.createPaymentUrl(order);
//        return "redirect:" + paymentUrl;
//    }
//
//    @GetMapping("/payment_return")
//    public String paymentReturn(@RequestParam Map<String, String> requestParams, Model model) {
//        // Xử lý phản hồi từ VNPay và cập nhật trạng thái đơn hàng
//        model.addAttribute("params", requestParams);
//        return "payment_result";
//    }
//}
