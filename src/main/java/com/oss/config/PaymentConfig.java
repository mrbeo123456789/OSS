package com.oss.config;

import org.springframework.beans.factory.annotation.Value;

import org.springframework.context.annotation.Configuration;

@Configuration
public class PaymentConfig {
    @Value("${vnpay.vnp_TmnCode}")
    private String vnp_TmnCode;

    @Value("${vnpay.vnp_HashSecret}")
    private String vnp_HashSecret;

    @Value("${vnpay.vnp_Url}")
    private String vnp_Url;

    public String getVnp_TmnCode() {
        return vnp_TmnCode;
    }

    public void setVnp_TmnCode(String vnp_TmnCode) {
        this.vnp_TmnCode = vnp_TmnCode;
    }

    public String getVnp_HashSecret() {
        return vnp_HashSecret;
    }

    public void setVnp_HashSecret(String vnp_HashSecret) {
        this.vnp_HashSecret = vnp_HashSecret;
    }

    public String getVnp_Url() {
        return vnp_Url;
    }

    public void setVnp_Url(String vnp_Url) {
        this.vnp_Url = vnp_Url;
    }
}
