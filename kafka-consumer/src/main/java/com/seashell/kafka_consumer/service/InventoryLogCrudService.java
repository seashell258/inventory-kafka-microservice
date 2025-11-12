package com.seashell.kafka_consumer.service;

import com.seashell.kafka_consumer.dto.EnrichedInventoryDto;

import com.seashell.kafka_consumer.entity.InventoryLogEntity;
import com.seashell.kafka_consumer.exception.InventoryNotFoundException;
import com.seashell.kafka_consumer.repository.InventoryLogRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class InventoryLogCrudService {

    private final InventoryLogRepository inventoryLogRepository;

    public InventoryLogCrudService(InventoryLogRepository inventoryLogRepository) {
        this.inventoryLogRepository = inventoryLogRepository;
    }

    // 會跟 inventory 更新一起在同一 Transaction 中更新 log
    public InventoryLogEntity insertInventoryLog(EnrichedInventoryDto dto) {

        InventoryLogEntity log = new InventoryLogEntity();

        log.setProductId(dto.getProductId());
        log.setOldQuantity(dto.getOldQuantity());
        log.setNewQuantity(dto.getNewQuantity());
        log.setQuantityChange(dto.getQuantityChange());
        log.setUpdatedAt(dto.getEventTime());
        log.setChangeReason(dto.getChangeReason() != null ? dto.getChangeReason() : "無備註");

        return inventoryLogRepository.save(log);

    }

    public List<InventoryLogEntity> insertBatchInventoryLog(List<EnrichedInventoryDto> dtoList) {
        List<InventoryLogEntity> logList = new ArrayList<>();
        for (EnrichedInventoryDto dto : dtoList) {
            InventoryLogEntity log = new InventoryLogEntity();
            log.setProductId(dto.getProductId());
            log.setOldQuantity(dto.getOldQuantity());
            log.setNewQuantity(dto.getNewQuantity());
            log.setQuantityChange(dto.getQuantityChange());
            log.setUpdatedAt(dto.getEventTime());
            log.setChangeReason(dto.getChangeReason() != null ? dto.getChangeReason() : "無備註");
            logList.add(log);
        }

        return inventoryLogRepository.saveAll(logList);

    }

    public InventoryLogEntity getInventoryLog(String productId) {
        return inventoryLogRepository.findByProductId(productId)
                .orElseThrow(() -> new InventoryNotFoundException(
                        "Inventory not found for productId: " + productId));
    }

    // 查全部
    public Page<InventoryLogEntity> getAll(Pageable pageable) {
        return inventoryLogRepository.findAll(pageable);
    }

}
