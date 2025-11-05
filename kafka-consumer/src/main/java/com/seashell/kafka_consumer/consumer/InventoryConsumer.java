package com.seashell.kafka_consumer.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.seashell.kafka_consumer.dto.InventoryMessageDTO;
import com.seashell.kafka_consumer.service.InventoryService;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class InventoryConsumer {

    private final InventoryService inventoryService;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    public InventoryConsumer(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

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
            inventoryService.upsertInventory(dto.getProductId(), dto.getQuantity());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
