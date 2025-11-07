package com.seashell.kafka_consumer.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.seashell.kafka_consumer.dto.InventoryMessageDTO;
import com.seashell.kafka_consumer.service.InventoryCrudService;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Set;

import com.seashell.kafka_consumer.dto.EnrichedInventoryDto;

@Service
public class InventoryConsumer {

    private final InventoryCrudService inventoryService;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    public InventoryConsumer(InventoryCrudService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @KafkaListener(topics = "inventory", groupId = "inventory-consumer-group")
    public void listen(String message) {
        System.out.println("Received: " + message);
        try {
            // JSON -> DTO
            EnrichedInventoryDto dto = objectMapper.readValue(message, EnrichedInventoryDto.class);

            // DTO 驗證
            Set<ConstraintViolation<EnrichedInventoryDto>> violations = validator.validate(dto);
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
