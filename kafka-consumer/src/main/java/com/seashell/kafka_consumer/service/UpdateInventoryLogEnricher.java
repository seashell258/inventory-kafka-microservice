package com.seashell.kafka_consumer.service;

import org.springframework.stereotype.Service;

import com.seashell.kafka_consumer.dto.EnrichedInventoryDto;
import com.seashell.kafka_consumer.dto.InventoryMessageDTO;
import com.seashell.kafka_consumer.entity.InventoryEntity;
import com.seashell.kafka_consumer.exception.InventoryNotFoundException;
import com.seashell.kafka_consumer.repository.InventoryRepository;

@Service
public class UpdateInventoryEnricher {

    private final InventoryRepository inventoryRepository;

    public UpdateInventoryEnricher(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    public EnrichedInventoryDto enrichInventoryMessage(InventoryMessageDTO dto) {
        // 查舊值，鎖住 row
        InventoryEntity entity = inventoryRepository
                .findByProductId(dto.getProductId())
                .orElseThrow(() -> new InventoryNotFoundException(
                "No inventory record found for productId: " + dto.getProductId()
        ));

        EnrichedInventoryDto enriched = new EnrichedInventoryDto();
        enriched.setProductId(dto.getProductId());
        enriched.setQuantityChange(dto.getQuantityChange());
        enriched.setOldQuantity(entity.getQuantity());
        enriched.setNewQuantity(entity.getQuantity() + dto.getQuantityChange());
        enriched.setLastUpdatedTimestamp(System.currentTimeMillis());
        enriched.setChangeReason(dto.getChangeReason()); // 如果原始 DTO 有備註欄位

        return enriched;
    }
}
