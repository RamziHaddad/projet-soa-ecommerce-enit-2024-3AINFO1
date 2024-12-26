package tn.soa_ecommerce.order.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import tn.soa_ecommerce.order.model.OrderStatus;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class MailingService {

    private final RestTemplate restTemplate;
    @Value("${mail.service.url}")
    private String mailServiceUrl;


    public MailingService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public boolean sendEmail(UUID orderId, UUID customerId, OrderStatus orderStatus, double amount) {
        String sendEmailUrl = mailServiceUrl + "/send";
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("orderId", orderId);
        requestBody.put("customerId", customerId);
        requestBody.put("orderStatus", orderStatus);
        requestBody.put("amount", amount);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);
        try{
            ResponseEntity<Boolean> response = restTemplate.postForEntity(sendEmailUrl, requestEntity, Boolean.class);
            return response.getBody() != null && response.getBody();
        } catch (Exception e) {
            return false;
        }
    }
}