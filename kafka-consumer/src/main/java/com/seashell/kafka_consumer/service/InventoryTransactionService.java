package com.seashell.kafka_consumer.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import com.seashell.kafka_consumer.dto.EnrichedInventoryDto;
import com.seashell.kafka_consumer.dto.InventoryBatchMessageDto;
import com.seashell.kafka_consumer.dto.InventoryMessageDto;

import jakarta.transaction.Transactional;

@Service
public class InventoryTransactionService {

    private final UpdateInventoryLogEnricher updateInventoryLogEnricher;
    private final InventoryLogCrudService inventoryLogCrudService;
    private final InventoryCrudService inventoryCrudService;

    public InventoryTransactionService(
            UpdateInventoryLogEnricher updateInventoryLogEnricher,
            InventoryLogCrudService inventoryLogCrudService,
            InventoryCrudService inventoryCrudService) {
        this.updateInventoryLogEnricher = updateInventoryLogEnricher;
        this.inventoryLogCrudService = inventoryLogCrudService;
        this.inventoryCrudService = inventoryCrudService;
    }

    @Transactional
    public void processInventoryMessage(InventoryMessageDto dto) {
        inventoryCrudService.getInventoryOrThrowWithLock(dto.getProductId()); // 健壯性檢查
        EnrichedInventoryDto enriched = updateInventoryLogEnricher.enrichInventoryMessageWithLock(dto); // 用舊的庫存量算新的庫存量，所以讀取的時候就加鎖了。
        inventoryLogCrudService.insertInventoryLog(enriched);
        inventoryCrudService.updateInventoryOrThrow(enriched);
    }

    @Transactional
    public Map<InventoryMessageDto, String> processInventoryBatchMessage(InventoryBatchMessageDto batchDto) {
        List<EnrichedInventoryDto> successList = new ArrayList<>();
        Map<InventoryMessageDto, String> errorMap = new HashMap<>();

        for (InventoryMessageDto dto : batchDto.getUpdates()) {
            try {
                inventoryCrudService.getInventoryOrThrow(dto.getProductId());
                EnrichedInventoryDto enriched = updateInventoryLogEnricher.enrichInventoryMessage(dto);
                successList.add(enriched);
            } catch (Exception e) {
                errorMap.put(dto, e.getMessage());
                System.out.println("cattttttttttttttttttt"+e.getMessage());
            }
        }

        inventoryLogCrudService.insertBatchInventoryLog(successList);
        inventoryCrudService.updateBatchInventoryOrThrow(successList);

        return errorMap;
    }

}
