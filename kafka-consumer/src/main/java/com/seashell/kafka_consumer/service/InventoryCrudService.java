package com.seashell.kafka_consumer.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.seashell.kafka_consumer.dto.EnrichedInventoryDto;
import com.seashell.kafka_consumer.entity.InventoryEntity;
import com.seashell.kafka_consumer.exception.InventoryNotFoundException;
import com.seashell.kafka_consumer.repository.InventoryRepository;
import org.springframework.stereotype.Service;

@Service
public class InventoryCrudService {

    private final InventoryRepository inventoryRepository;

    public InventoryCrudService(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    // 更新庫存
    public InventoryEntity updateInventoryOrThrow(EnrichedInventoryDto dto) {
        InventoryEntity entity = this.getInventoryOrThrow(dto.getProductId()); // 健壯性檢查
        entity.setQuantity(dto.getNewQuantity());
        entity.setLastUpdatedTimestamp(dto.getEventTime());
        return inventoryRepository.save(entity); 

    }

    // 健壯性檢查：找不到就拋 exception
    public InventoryEntity getInventoryOrThrow(String productId) {
        return inventoryRepository.findByProductId(productId)
                .orElseThrow(() -> new InventoryNotFoundException(
                        "Inventory not found for productId: " + productId));
    }

    // 刪除庫存項
    public void deleteInventory(String productId) {
        inventoryRepository.findByProductId(productId)
                .ifPresent(inventoryRepository::delete);
    }

    // 分頁查全部
    public Page<InventoryEntity> getAll(Pageable pageable) {
        return inventoryRepository.findAll(pageable);
    }

}
