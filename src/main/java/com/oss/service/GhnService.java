package com.oss.service;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.oss.config.GhnApiConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class GhnService {

    private final GhnApiConfig ghnApiConfig;
    private final RestTemplate restTemplate;

    @Autowired
    public GhnService(GhnApiConfig ghnApiConfig, RestTemplate restTemplate) {
        this.ghnApiConfig = ghnApiConfig;
        this.restTemplate = restTemplate;
    }

    public Map<String, String> getProvinces() {
        String url = ghnApiConfig.getApiUrl() + "/province";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Token", ghnApiConfig.getApiKey());
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        JsonElement jsonElement = JsonParser.parseString(response.getBody());
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        JsonArray provincesArray = jsonObject.getAsJsonArray("data"); // Giả sử dữ liệu nằm trong mảng "data"

        Map<String, String> provincesMap = new HashMap<>();
        for (JsonElement provinceElement : provincesArray) {
            JsonObject provinceObject = provinceElement.getAsJsonObject();
            String id = provinceObject.get("ProvinceID").getAsString(); // Giả sử ID là "ProvinceID"
            String name = provinceObject.get("ProvinceName").getAsString(); // Giả sử tên là "ProvinceName"
            provincesMap.put(id, name);
        }

        return provincesMap;
    }

    public Map<String, String> getDistricts(String provinceId) {
        String url = ghnApiConfig.getApiUrl() + "/district?province_id=" + provinceId;
        HttpHeaders headers = new HttpHeaders();
        headers.set("Token", ghnApiConfig.getApiKey());
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        JsonElement jsonElement = JsonParser.parseString(response.getBody());
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        JsonArray districtsArray = jsonObject.getAsJsonArray("data");

        Map<String, String> districtsMap = new HashMap<>();
        for (JsonElement districtElement : districtsArray) {
            JsonObject districtObject = districtElement.getAsJsonObject();
            String id = districtObject.get("DistrictID").getAsString();
            String name = districtObject.get("DistrictName").getAsString();
            districtsMap.put(id, name);
        }

        return districtsMap;
    }

    public Map<String, String> getWards(String districtId) {
        String url = ghnApiConfig.getApiUrl() + "/ward?district_id=" + districtId;
        HttpHeaders headers = new HttpHeaders();
        headers.set("Token", ghnApiConfig.getApiKey());
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        JsonElement jsonElement = JsonParser.parseString(response.getBody());
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        JsonArray wardsArray = jsonObject.getAsJsonArray("data");

        Map<String, String> wardsMap = new HashMap<>();
        for (JsonElement wardElement : wardsArray) {
            JsonObject wardObject = wardElement.getAsJsonObject();
            String id = wardObject.get("WardCode").getAsString();
            String name = wardObject.get("WardName").getAsString();
            wardsMap.put(id, name);
        }

        return wardsMap;
    }

    public double calculateShippingFee(String toWard, String toDistrictId, int weight, int length, int width, int height) {
        String url = "https://online-gateway.ghn.vn/shiip/public-api/v2/shipping-order/fee";
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set("Token", "b2ba9319-16c9-11ef-be43-f60ece3fb56e");
        headers.set("ShopId", "5079342");

        JsonObject requestBody = new JsonObject();
        requestBody.addProperty("from_district_id", 1454);
        requestBody.addProperty("from_ward_code", "21211");
        requestBody.addProperty("service_id", 53320);
        requestBody.add("service_type_id", null);
        requestBody.addProperty("to_district_id", Integer.parseInt(toDistrictId));
        requestBody.addProperty("to_ward_code", toWard);
        requestBody.addProperty("height", 50);
        requestBody.addProperty("length", 20);
        requestBody.addProperty("weight", 200);
        requestBody.addProperty("width", 20);
        requestBody.addProperty("insurance_value", 10000);
        requestBody.addProperty("cod_failed_amount", 2000);
        requestBody.add("coupon", null);

        HttpEntity<String> entity = new HttpEntity<>(requestBody.toString(), headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
            JsonObject jsonObject = JsonParser.parseString(response.getBody()).getAsJsonObject();
            if (jsonObject.has("data")) {
                JsonObject data = jsonObject.getAsJsonObject("data");
                if (data.has("total")) {
                    return data.get("total").getAsDouble();
                }
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
        return 0;
    }



}

