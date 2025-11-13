package com.seashell.kafka_consumer.consumer;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

import com.seashell.kafka_consumer.dto.InventoryBatchMessageDto;
import com.seashell.kafka_consumer.dto.InventoryMessageDto;
import com.seashell.kafka_consumer.exception.InvalidDtoException;
import com.seashell.kafka_consumer.service.InventoryTransactionService;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InventoryConsumer {
    private final InventoryTransactionService inventoryTransactionService;

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @KafkaListener(topics = "inventory", 
    groupId = "inventory-consumer-group",
    containerFactory = "singleConsumerListenerFactory" )
    public void listen(InventoryMessageDto dto, Acknowledgment ack) throws Exception {
        System.out.println(dto);

        // DTO 驗證
        Set<ConstraintViolation<InventoryMessageDto>> violations = validator.validate(dto);
        if (!violations.isEmpty()) {

            String msg = violations.stream()
                    .map(v -> v.getPropertyPath() + ": " + v.getMessage()) // 可加 field 名稱
                    .collect(Collectors.joining(", "));

            throw new InvalidDtoException("dto validation error:" + msg, dto);
        }

        // 呼叫 Service 層處理業務邏輯
        inventoryTransactionService.processInventoryMessage(dto);
        // throw new retrytest("everyting goes well, but I want to test retry ");

        System.out.println("about to ack  ");
        ack.acknowledge();

    }

    @KafkaListener(topics = "batch-inventory"
    , groupId = "inventory-consumer-group"
    ,containerFactory = "batchConsumerListenerFactory")
    public void listenBatch(InventoryBatchMessageDto batchDto, Acknowledgment ack) throws Exception {

        System.out.println("Received: " );
        // DTO 驗證
        Set<ConstraintViolation<InventoryBatchMessageDto>> violations = validator.validate(batchDto);
        if (!violations.isEmpty()) {
            String msg = violations.stream()
                    .map(v -> v.getPropertyPath() + ": " + v.getMessage()) // 可加 field 名稱
                    .collect(Collectors.joining(", "));

            throw new InvalidDtoException("dto validation error:" + msg, batchDto);

        }

        // 呼叫 Service 層處理業務邏輯 入庫。 (error handler分類狀況:目前裡面我只看到可能有 inventory not found
        // exception
        inventoryTransactionService.processInventoryBatchMessage(batchDto);

        System.out.println("about to ack single batch");
        ack.acknowledge(); // 整批處理完再提交 offset*/

    }
}
