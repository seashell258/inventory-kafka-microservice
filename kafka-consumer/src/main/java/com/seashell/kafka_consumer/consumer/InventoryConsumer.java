package com.seashell.kafka_consumer.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.seashell.kafka_consumer.dto.InventoryBatchMessageDto;
import com.seashell.kafka_consumer.dto.InventoryMessageDto;

import com.seashell.kafka_consumer.service.InventoryTransactionService;


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
    private final InventoryTransactionService inventoryTransactionService;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @KafkaListener(topics = "inventory", groupId = "inventory-consumer-group")
    public void listen(String message) {
        System.out.println("Received: " + message);
        try {
            // JSON -> DTO
            InventoryMessageDto dto = objectMapper.readValue(message, InventoryMessageDto.class);
            // DTO 驗證
            Set<ConstraintViolation<InventoryMessageDto>> violations = validator.validate(dto);
            if (!violations.isEmpty()) {
                violations.forEach(v -> System.err.println("Validation error: " + v.getMessage()));
                return; // 有錯就不呼叫 service
            }

            // 呼叫 Service 層處理業務邏輯
            inventoryTransactionService.processInventoryMessage(dto);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

     @KafkaListener(topics = "batch-inventory", groupId = "inventory-consumer-group")
    public void listenBatch(String message) {
        System.out.println("Received: " + message);
        try {
            // JSON -> DTO
            InventoryBatchMessageDto dto = objectMapper.readValue(message, InventoryBatchMessageDto.class);
            // DTO 驗證
            Set<ConstraintViolation<InventoryBatchMessageDto>> violations = validator.validate(dto);
            if (!violations.isEmpty()) {
                violations.forEach(v -> System.err.println("Validation error: " + v.getMessage()));
                return; // 有錯就不呼叫 service
            }

            // 呼叫 Service 層處理業務邏輯 入庫
            inventoryTransactionService.processInventoryBatchMessage(dto);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
