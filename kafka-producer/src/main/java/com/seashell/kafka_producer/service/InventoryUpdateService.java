package com.seashell.kafka_producer.service;

import java.time.Instant;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.seashell.kafka_producer.dto.InventoryUpdateDto;
import com.seashell.kafka_producer.dto.OperationResult;

@Service
//@Component 的專用化，用於標註服務層的 Bean
public class InventoryUpdateService {

    private final KafkaTemplate<String, InventoryUpdateDto> kafkaTemplate;

    public InventoryUpdateService(KafkaTemplate<String, InventoryUpdateDto> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void fillEventTime(InventoryUpdateDto dto) {
        dto.setEventTime(Instant.now().toString());
    }

    public OperationResult publishInventoryUpdate(InventoryUpdateDto dto) {
        this.fillEventTime(dto);
        try {
            kafkaTemplate.send("inventory", dto.getProductId(), dto);
            return OperationResult.success("successfully published to kafka");
        } catch (Exception e) {
            e.printStackTrace();
            return OperationResult.failure(e.getMessage());
        }
    }


}
