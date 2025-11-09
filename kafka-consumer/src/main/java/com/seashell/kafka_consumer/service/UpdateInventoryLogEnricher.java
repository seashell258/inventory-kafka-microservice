package com.seashell.kafka_consumer.service;

import java.time.Instant;

import org.springframework.stereotype.Service;

import com.seashell.kafka_consumer.dto.EnrichedInventoryDto;
import com.seashell.kafka_consumer.dto.InventoryMessageDTO;
import com.seashell.kafka_consumer.entity.InventoryEntity;
import com.seashell.kafka_consumer.exception.InventoryNotFoundException;
import com.seashell.kafka_consumer.repository.InventoryRepository;

@Service
public class UpdateInventoryLogEnricher {

    private final InventoryRepository inventoryRepository;

    public UpdateInventoryLogEnricher(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    public long formatTimeToInstant(InventoryMessageDTO dto) {
        Instant instant = Instant.parse(dto.getEventTime());
        long timestamp = instant.toEpochMilli();
        return timestamp;
    }

    public EnrichedInventoryDto enrichInventoryMessageWithLock(InventoryMessageDTO dto) {
        long timestamp = this.formatTimeToInstant(dto);
        // 查舊值，鎖住 row
        InventoryEntity entity = inventoryRepository
                .findByProductId(dto.getProductId())
                .orElseThrow(() -> new InventoryNotFoundException(
                "No inventory record found for productId: " + dto.getProductId()
        ));

        EnrichedInventoryDto enriched = EnrichedInventoryDto.builder()
                .productId(dto.getProductId())
                .quantityChange(dto.getQuantityChange())
                .oldQuantity(entity.getQuantity())
                .newQuantity(entity.getQuantity() + dto.getQuantityChange())
                .eventTime(timestamp)
                .changeReason(dto.getChangeReason() != null ? dto.getChangeReason() : "無備註")
                .build();

        return enriched;
    }
}
