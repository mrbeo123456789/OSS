//package com.oss.repository;
//import java.net.URLEncoder;
//import java.nio.charset.StandardCharsets;
//import java.util.Map;
//import java.util.stream.Collectors;
//import javax.crypto.Mac;
//import javax.crypto.spec.SecretKeySpec;
//import java.util.Base64;
//public class VNPayUtils {
//    public static String createQueryUrl(Map<String, String> params, String vnp_HashSecret) {
//        String hashData = params.entrySet().stream()
//                .sorted(Map.Entry.comparingByKey())
//                .map(entry -> entry.getKey() + "=" + URLEncoder.encode(entry.getValue(), StandardCharsets.US_ASCII))
//                .collect(Collectors.joining("&"));
//
//        String secureHash = hmacSHA512(vnp_HashSecret, hashData);
//        params.put("vnp_SecureHash", secureHash);
//
//        return params.entrySet().stream()
//                .map(entry -> entry.getKey() + "=" + URLEncoder.encode(entry.getValue(), StandardCharsets.US_ASCII))
//                .collect(Collectors.joining("&"));
//    }
//
//    private static String hmacSHA512(String key, String data) {
//        try {
//            Mac hmacSHA512 = Mac.getInstance("HmacSHA512");
//            SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(), "HmacSHA512");
//            hmacSHA512.init(secretKey);
//            return Base64.getEncoder().encodeToString(hmacSHA512.doFinal(data.getBytes()));
//        } catch (Exception e) {
//            throw new RuntimeException("Error while hashing data", e);
//        }
//    }
//}
