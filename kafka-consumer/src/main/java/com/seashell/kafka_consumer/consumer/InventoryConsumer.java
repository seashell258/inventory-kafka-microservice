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
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class InventoryConsumer {
    private final InventoryTransactionService inventoryTransactionService;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @KafkaListener(topics = "inventory", groupId = "inventory-consumer-group")
    public void listen(List<String> messages, Acknowledgment ack) throws Exception {
        System.out.println("Receivedcatcat: " + messages);
        /*for (String message : messages) {
            System.out.println("Received: " + message);
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
        }
        ack.acknowledge();*/
    }

    @KafkaListener(topics = "batch-inventory", groupId = "inventory-consumer-group")
    public void listenBatch(List<String> messages, Acknowledgment ack) throws Exception {
        System.out.println("Receivedbatchbatch before for: " + messages);
       throw new RuntimeException("force retry");
        /*for (String message : messages) { //一個message裡面就是一個能拿來批量入庫的 list 了。 messages 是一次多 pull 下來的多筆 list
             System.out.println("Received: " + message);
            // JSON -> DTO
            InventoryBatchMessageDto batchDto = objectMapper.readValue(message, InventoryBatchMessageDto.class);
            // DTO 驗證
            Set<ConstraintViolation<InventoryBatchMessageDto>> violations = validator.validate(dto);
            if (!violations.isEmpty()) {
                violations.forEach(v -> System.err.println("Validation error: " + v.getMessage()));
                return; // 有錯就不呼叫 service
            }

            // 呼叫 Service 層處理業務邏輯 入庫
            inventoryTransactionService.processInventoryBatchMessage(batchDto);
        }
        ack.acknowledge(); // 整批處理完再提交 offset*/

    }
}
