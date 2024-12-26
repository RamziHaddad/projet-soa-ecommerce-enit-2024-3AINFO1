package tn.soa_ecommerce.order.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import tn.soa_ecommerce.order.dto.OrderItemDTO;
import tn.soa_ecommerce.order.mapper.Mapper;
import tn.soa_ecommerce.order.model.OrderItem;

import java.util.*;

@Service
public class InventoryService {

    private final RestTemplate restTemplate;
    @Value("${inventory.service.url}")
    private String inventoryServiceUrl;
    private final Mapper<OrderItem, OrderItemDTO> orderItemMapper;
    public InventoryService(RestTemplate restTemplate,
                            Mapper<OrderItem, OrderItemDTO> orderItemMapper) {
        this.restTemplate = restTemplate;
        this.orderItemMapper = orderItemMapper;
    }

    public boolean reserveProducts(UUID orderId, List<OrderItem> products) {

        List<OrderItemDTO> productsDTO = new ArrayList<>();
        for (OrderItem product: products) {
           productsDTO.add(orderItemMapper.mapTo(product));
        }
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("orderId", orderId);
        requestBody.put("products", productsDTO);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<Boolean> response = restTemplate.postForEntity(inventoryServiceUrl, requestEntity, Boolean.class);
        return response.getBody() != null && response.getBody();
    }
}