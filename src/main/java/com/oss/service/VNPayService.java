//package com.oss.service;
//import com.oss.config.PaymentConfig;
//import com.oss.repository.VNPayUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.HashMap;
//import java.util.Map;
//import com.oss.model.Order;
//
//@Service
//public class VNPayService {
//    @Autowired
//    private PaymentConfig paymentConfig;
//
//    public String createPaymentUrl(Order order) {
//        System.out.println(order.getOrderId());
//
//        // Prepare payment parameters
//        String vnp_TxnRef = String.valueOf(System.currentTimeMillis());
//        String vnp_OrderInfo = "Thanh toan don hang: " + order.getOrderId();
//        String vnp_OrderType = "other";
//        String vnp_Amount = String.valueOf((int) (order.getTotalAmount() * 100)); // Convert to integer value in cents
//        String vnp_Locale = "vn";
//        String vnp_BankCode = "";
//        String vnp_IpAddr = "127.0.0.1";
//        String vnp_ReturnUrl = "http://localhost:8080/payment_return";
//
//        // Create parameter map
//        Map<String, String> vnp_Params = new HashMap<>();
//        vnp_Params.put("vnp_Version", "2.1.0");
//        vnp_Params.put("vnp_Command", "pay");
//        vnp_Params.put("vnp_TmnCode", paymentConfig.getVnp_TmnCode());
//        vnp_Params.put("vnp_Amount", vnp_Amount);
//        vnp_Params.put("vnp_CurrCode", "VND");
//        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
//        vnp_Params.put("vnp_OrderInfo", vnp_OrderInfo);
//        vnp_Params.put("vnp_OrderType", vnp_OrderType);
//        vnp_Params.put("vnp_Locale", vnp_Locale);
//        vnp_Params.put("vnp_ReturnUrl", vnp_ReturnUrl);
//        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);
//
//        // Add security hash
//        String queryUrl = VNPayUtils.createQueryUrl(vnp_Params, paymentConfig.getVnp_HashSecret());
//
//        // Return the full payment URL
//        return paymentConfig.getVnp_Url() + "?" + queryUrl;
//    }
//
//}
