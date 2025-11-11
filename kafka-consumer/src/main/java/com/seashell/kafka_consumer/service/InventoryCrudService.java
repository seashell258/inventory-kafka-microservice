package com.seashell.kafka_consumer.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.seashell.kafka_consumer.dto.EnrichedInventoryDto;
import com.seashell.kafka_consumer.entity.InventoryEntity;
import com.seashell.kafka_consumer.exception.InventoryNotFoundException;
import com.seashell.kafka_consumer.repository.InventoryRepository;

@Service
public class InventoryCrudService {

    private final InventoryRepository inventoryRepository;

    public InventoryCrudService(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    // 更新庫存
    public InventoryEntity updateInventoryOrThrow(EnrichedInventoryDto dto) {
        InventoryEntity entity = this.getInventoryOrThrowWithLock(dto.getProductId()); // 健壯性檢查
        entity.setQuantity(dto.getNewQuantity());
        return inventoryRepository.save(entity);

    }

    // 健壯性檢查：找不到就拋 exception
    public InventoryEntity getInventoryOrThrowWithLock(String productId) {
        return inventoryRepository.findByProductIdWithLock(productId)
                .orElseThrow(() -> new InventoryNotFoundException(
                        "Inventory not found for productId: " + productId));
    }

    public InventoryEntity getInventoryOrThrow(String productId) {
        return inventoryRepository.findByProductId(productId)
                .orElseThrow(() -> new InventoryNotFoundException(
                        "Inventory not found for productId: " + productId));

    }

    public List<InventoryEntity> updateBatchInventoryOrThrow(List<EnrichedInventoryDto> batchDto) {
        List<InventoryEntity> inventoryEntityList =  new ArrayList<>();
        for (EnrichedInventoryDto dto : batchDto) {
            InventoryEntity entity = this.getInventoryOrThrow(dto.getProductId()); // 健壯性檢查
            entity.setQuantity(dto.getNewQuantity());
            inventoryEntityList.add(entity);
        }
        return inventoryRepository.saveAll(inventoryEntityList);
    }

    



    // 刪除庫存項
    public void deleteInventory(String productId) {
        inventoryRepository.findByProductIdWithLock(productId)
                .ifPresent(inventoryRepository::delete);
    }

    // 分頁查全部
    public Page<InventoryEntity> getAll(Pageable pageable) {
        return inventoryRepository.findAll(pageable);
    }

}
