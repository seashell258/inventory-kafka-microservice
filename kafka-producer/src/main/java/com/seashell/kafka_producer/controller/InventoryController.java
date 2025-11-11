package com.seashell.kafka_producer.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.seashell.kafka_producer.dto.InventoryUpdateBatchDto;
import com.seashell.kafka_producer.dto.InventoryUpdateDto;
import com.seashell.kafka_producer.dto.OperationResult;
import com.seashell.kafka_producer.service.InventoryBatchUpdateService;
import com.seashell.kafka_producer.service.InventoryUpdateService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/inventory")
public class InventoryController {

    private final InventoryUpdateService service;
    private final InventoryBatchUpdateService batchService;

    public InventoryController(InventoryUpdateService service, InventoryBatchUpdateService batchService) {
        this.service = service;
        this.batchService = batchService;
    }

    @PostMapping("/update")
    public OperationResult updateInventory(@RequestBody @Valid InventoryUpdateDto dto) {
        OperationResult result = service.publishInventoryUpdate(dto);
        return result;
    }

    @PostMapping("/batch-update")
    public OperationResult batchUpdateInventory(@RequestBody @Valid InventoryUpdateBatchDto batchDto) {
        OperationResult result = batchService.publishInventoryBatchUpdate(batchDto);
        return result;
    }
}