package com.seashell.kafka_consumer.service;

import com.seashell.kafka_consumer.entity.InventoryEntity;
import com.seashell.kafka_consumer.repository.InventoryRepository;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.Optional;

@Service
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    public InventoryService(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    // 新增或更新庫存
    public InventoryEntity upsertInventory(String productId, int quantity) {
        Optional<InventoryEntity> existing = inventoryRepository.findByProductId(productId);

        InventoryEntity entity;
        if (existing.isPresent()) {
            // 如果已存在 → 修改數量
            entity = existing.get();
            entity.setQuantity(entity.getQuantity() + quantity);
        } else {
            // 如果不存在 → 新增新商品
            entity = new InventoryEntity(productId, quantity, Instant.now().toEpochMilli());
        }

        entity.setLastUpdatedTimestamp(Instant.now().toEpochMilli());
        return inventoryRepository.save(entity); // save = 新增或更新
    }

    // 查庫存 // findBy+entity裡面有的名稱，可以自動創建 crud 方法。
    public Optional<InventoryEntity> getInventory(String productId) {
        return inventoryRepository.findByProductId(productId);
    }

    // 刪除庫存項
    public void deleteInventory(String productId) {
        inventoryRepository.findByProductId(productId)
                .ifPresent(inventoryRepository::delete);
    }

    // 查全部
    public Iterable<InventoryEntity> getAll() {
        return inventoryRepository.findAll();
    }
}
