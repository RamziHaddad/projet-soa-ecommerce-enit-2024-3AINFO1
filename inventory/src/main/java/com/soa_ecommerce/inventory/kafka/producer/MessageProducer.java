package com.soa_ecommerce.inventory.kafka.producer;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class MessageProducer {


    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public MessageProducer(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public void sendMessage(String topic, Object message) {
        try {
            String messageJson = objectMapper.writeValueAsString(message);
            kafkaTemplate.send(topic, messageJson);
        } catch (Exception e) {
            throw new RuntimeException("Failed to send message to topic: " + topic, e);
        }
    }



}
