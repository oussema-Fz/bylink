package com.bylink.service;

import com.bylink.entity.Transaction;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class DeliveryService {

    @Value("${delivery.api.url}")
    private String deliveryApiUrl;

    @Value("${delivery.api.key}")
    private String deliveryApiKey;

    private final RestTemplate restTemplate;

    public DeliveryService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String scheduleDelivery(Transaction transaction) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-API-Key", deliveryApiKey);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("orderId", transaction.getId());
        requestBody.put("pickupAddress", transaction.getReceiver().getAddress());
        requestBody.put("deliveryAddress", transaction.getSender().getAddress());
        requestBody.put("productName", transaction.getProduct().getName());
        requestBody.put("contactName", transaction.getSender().getFirstName() + " " + transaction.getSender().getLastName());
        requestBody.put("contactPhone", transaction.getSender().getPhoneNumber());

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);
        
        Map<String, Object> response = restTemplate.postForObject(
            deliveryApiUrl + "/schedule",
            request,
            Map.class
        );

        return (String) response.get("trackingId");
    }

    public Map<String, Object> getDeliveryStatus(String trackingId) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-API-Key", deliveryApiKey);

        HttpEntity<?> request = new HttpEntity<>(headers);
        
        return restTemplate.getForObject(
            deliveryApiUrl + "/status/" + trackingId,
            Map.class,
            request
        );
    }
}
