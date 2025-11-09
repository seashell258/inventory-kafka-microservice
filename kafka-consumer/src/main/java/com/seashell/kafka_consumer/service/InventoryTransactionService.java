package com.seashell.kafka_consumer.service;

import org.springframework.stereotype.Service;
import com.seashell.kafka_consumer.dto.EnrichedInventoryDto;
import com.seashell.kafka_consumer.dto.InventoryMessageDTO;


import jakarta.transaction.Transactional;   

@Service
public class InventoryTransactionService {

    private final UpdateInventoryLogEnricher updateInventoryLogEnricher;
    private final InventoryLogCrudService inventoryLogCrudService;
    private final InventoryCrudService inventoryCrudService;

    public InventoryTransactionService(
            UpdateInventoryLogEnricher updateInventoryLogEnricher,
            InventoryLogCrudService inventoryLogCrudService,
            InventoryCrudService inventoryCrudService
    ) {
        this.updateInventoryLogEnricher = updateInventoryLogEnricher;
        this.inventoryLogCrudService = inventoryLogCrudService;
        this.inventoryCrudService = inventoryCrudService;
    }



    @Transactional
    public void processInventoryMessage(InventoryMessageDTO dto) {
        inventoryCrudService.getInventoryOrThrow(dto.getProductId()); //健壯性檢查 
        EnrichedInventoryDto enriched = updateInventoryLogEnricher.enrichInventoryMessageWithLock(dto); //用舊的庫存量算新的庫存量，所以讀取的時候就加鎖了。
        inventoryLogCrudService.insertInventoryLog(enriched);
        inventoryCrudService.updateInventoryOrThrow(enriched);
    }
}
