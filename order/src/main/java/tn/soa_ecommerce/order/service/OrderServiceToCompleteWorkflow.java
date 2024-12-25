package tn.soa_ecommerce.order.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class OrderServiceToCompleteWorkflow {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    // Inject Kafka topics from application.properties
    @Value("${kafka.topic.inventory.reserve.request}")
    private String inventoryReserveTopic;

    @Value("${kafka.topic.payment.process.request}")
    private String paymentProcessTopic;

    @Value("${kafka.topic.shipping.schedule.request}")
    private String shippingScheduleTopic;

    @Value("${kafka.topic.notification.send.request}")
    private String notificationSendTopic;

    @Value("${kafka.topic.inventory.reserve.result}")
    private String inventoryReserveResultTopic;

    @Value("${kafka.topic.payment.process.result}")
    private String paymentProcessResultTopic;

    @Value("${kafka.topic.shipping.schedule.result}")
    private String shippingScheduleResultTopic;

    @Value("${kafka.topic.notification.send.result}")
    private String notificationSendResultTopic;

    public OrderServiceToCompleteWorkflow(KafkaTemplate<String, String> kafkaTemplate,
                                          ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public void sendKafkaMessage(String topic, String key, Map<String, Object> message) {
        try {
            String messageString = objectMapper.writeValueAsString(message);
            kafkaTemplate.send(topic, key, messageString);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Kafka consumer for INVENTORY_RESERVE_TOPIC
    @KafkaListener(topics = "${kafka.topic.inventory.reserve.request}", groupId = "order-group")
    public void consumeInventoryReserveRequest(String message) {
        try {
            Map<String, Object> request = objectMapper.readValue(message, Map.class);
            UUID orderId = UUID.fromString(request.get("orderId").toString());
            boolean inventorySuccess = true; // success or failure

            simulateInventoryReserveResult(orderId, inventorySuccess);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void simulateInventoryReserveResult(UUID orderId, boolean success) {
        Map<String, Object> inventoryReserveResult = new HashMap<>();
        inventoryReserveResult.put("orderId", orderId.toString());
        inventoryReserveResult.put("success", success);

        sendKafkaMessage(inventoryReserveResultTopic, orderId.toString(), inventoryReserveResult);
    }

    // Kafka consumer for PAYMENT_PROCESS_TOPIC
    @KafkaListener(topics = "${kafka.topic.payment.process.request}", groupId = "order-group")
    public void consumePaymentProcessRequest(String message) {
        try {
            Map<String, Object> request = objectMapper.readValue(message, Map.class);
            UUID orderId = UUID.fromString(request.get("orderId").toString());
            boolean paymentSuccess = true; // success or failure

            simulatePaymentProcessResult(orderId, paymentSuccess);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void simulatePaymentProcessResult(UUID orderId, boolean success) {
        Map<String, Object> paymentProcessResult = new HashMap<>();
        paymentProcessResult.put("orderId", orderId.toString());
        paymentProcessResult.put("success", success);

        sendKafkaMessage(paymentProcessResultTopic, orderId.toString(), paymentProcessResult);
    }

    // Kafka consumer for SHIPPING_SCHEDULE_TOPIC
    @KafkaListener(topics = "${kafka.topic.shipping.schedule.request}", groupId = "order-group")
    public void consumeShippingScheduleRequest(String message) {
        try {
            Map<String, Object> request = objectMapper.readValue(message, Map.class);
            UUID orderId = UUID.fromString(request.get("orderId").toString());
            boolean shippingSuccess = true; // success or failure

            simulateShippingScheduleResult(orderId, shippingSuccess);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void simulateShippingScheduleResult(UUID orderId, boolean success) {
        Map<String, Object> shippingScheduleResult = new HashMap<>();
        shippingScheduleResult.put("orderId", orderId.toString());
        shippingScheduleResult.put("success", success);

        sendKafkaMessage(shippingScheduleResultTopic, orderId.toString(), shippingScheduleResult);
    }

    // Kafka consumer for NOTIFICATION_SEND_TOPIC
    @KafkaListener(topics = "${kafka.topic.notification.send.request}", groupId = "order-group")
    public void consumeNotificationSendRequest(String message) {
        try {
            Map<String, Object> request = objectMapper.readValue(message, Map.class);
            UUID orderId = UUID.fromString(request.get("orderId").toString());
            boolean notificationSuccess = true; // success or failure

            simulateNotificationSendResult(orderId, notificationSuccess);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void simulateNotificationSendResult(UUID orderId, boolean success) {
        Map<String, Object> notificationSendResult = new HashMap<>();
        notificationSendResult.put("orderId", orderId.toString());
        notificationSendResult.put("success", success);

        sendKafkaMessage(notificationSendResultTopic, orderId.toString(), notificationSendResult);
    }
}