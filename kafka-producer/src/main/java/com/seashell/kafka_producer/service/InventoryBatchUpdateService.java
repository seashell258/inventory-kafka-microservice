package com.seashell.kafka_producer.service;

import java.time.Instant;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.seashell.kafka_producer.dto.InventoryUpdateBatchDto;
import com.seashell.kafka_producer.dto.OperationResult;

@Service
public class InventoryBatchUpdateService {
    
    private final KafkaTemplate<String, InventoryUpdateBatchDto> kafkaTemplate;

    public InventoryBatchUpdateService(KafkaTemplate<String, InventoryUpdateBatchDto> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void fillEventTime(InventoryUpdateBatchDto batchDto) {
        batchDto.getUpdates()
        .forEach(update -> update.setEventTime(Instant.now().toString()));
    }

    public OperationResult publishInventoryBatchUpdate(InventoryUpdateBatchDto batchDto) {
        this.fillEventTime(batchDto);
        try {
            kafkaTemplate.send("inventory", null, batchDto);
            return OperationResult.success("batch successfully published to kafka");
        } catch (Exception e) {
            e.printStackTrace();
            return OperationResult.failure(e.getMessage());
        }
    }


}
