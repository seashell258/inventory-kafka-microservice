package com.seashell.kafka_producer.service;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;


import com.seashell.kafka_producer.dto.OperationResult;

@Service
//@Component 的專用化，用於標註服務層的 Bean
public class ProducerInventoryUpdateService {
    private final KafkaTemplate<String, String> kafkaTemplate;

    public ProducerInventoryUpdateService(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }
      public OperationResult publishInventoryUpdate(String productId, int quantityChange) {
        try {
            String message = productId + ":" + quantityChange;
            kafkaTemplate.send("inventory", productId, message);
            return OperationResult.success("successfully published to kafka");
        } catch (Exception e) {
            e.printStackTrace();
            return OperationResult.failure(e.getMessage());
        }
    }
}
