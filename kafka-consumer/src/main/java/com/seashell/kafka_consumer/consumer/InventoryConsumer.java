package com.seashell.kafka_consumer.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.seashell.kafka_consumer.dto.InventoryMessageDTO;

import com.seashell.kafka_consumer.service.InventoryProcessingService;


import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Set;


@Service
@RequiredArgsConstructor
public class InventoryConsumer {
    private final InventoryProcessingService inventoryProcessingService;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @KafkaListener(topics = "inventory", groupId = "inventory-consumer-group")
    public void listen(String message) {
        System.out.println("Received: " + message);
        try {
            // JSON -> DTO
            InventoryMessageDTO dto = objectMapper.readValue(message, InventoryMessageDTO.class);
            // DTO 驗證
            Set<ConstraintViolation<InventoryMessageDTO>> violations = validator.validate(dto);
            if (!violations.isEmpty()) {
                violations.forEach(v -> System.err.println("Validation error: " + v.getMessage()));
                return; // 有錯就不呼叫 service
            }

            // 呼叫 Service 層處理業務邏輯
            inventoryProcessingService.processInventoryMessage(dto);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
