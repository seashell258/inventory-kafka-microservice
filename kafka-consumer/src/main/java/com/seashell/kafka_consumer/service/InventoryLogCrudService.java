package com.seashell.kafka_consumer.service;

import com.seashell.kafka_consumer.entity.InventoryEntity;
import com.seashell.kafka_consumer.entity.InventoryLogEntity;
import com.seashell.kafka_consumer.repository.InventoryLogRepository;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.Optional;

@Service
public class InventoryLogCrudService {

    private final InventoryLogRepository inventoryLogRepository;


    public InventoryLogCrudService(InventoryLogRepository inventoryLogRepository) {
        this.inventoryLogRepository = inventoryLogRepository;
    }

    // 新增庫存更新 log
    public InventoryLogEntity upsertInventoryLog(String productId, int quantityChange) {
        Optional<InventoryLogEntity> existing = inventoryLogRepository.findByProductId(productId);

        InventoryLogEntity entity;
        if (existing.isPresent()) {
            // 如果已存在 → 修改數量
            entity = existing.get(); 
            entity.setNewQuantity(entity.getOldQuantity() + quantityChange); //新值
             entity.setOldQuantity(entity.getQuantity()); //新值
        } else {
            // 如果不存在 → 新增新商品
           .orElseThrow(() -> new InventoryNotFoundException(
                "No inventory record found for productId: " + dto.getProductId()
        ));
        }

        entity.setLastUpdatedTimestamp(Instant.now().toEpochMilli());
        return inventoryLogRepository.save(entity); // save = 新增或更新
    }

    public Optional<InventoryEntity> getInventoryLog(String productId) {
        return inventoryLogRepository.findByProductId(productId);
    }

    // 查全部
    public Iterable<InventoryEntity> getAll() {
        return inventoryLogRepository.findAll();
    }
}
