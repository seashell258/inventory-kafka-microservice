package com.seashell.kafka_producer.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.seashell.kafka_producer.dto.InventoryUpdateDto;
import com.seashell.kafka_producer.dto.OperationResult;
import com.seashell.kafka_producer.service.ProducerInventoryUpdateService;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/inventory")
public class InventoryController {

    private final ProducerInventoryUpdateService service;

    public InventoryController(ProducerInventoryUpdateService service) {
        this.service = service;
    }

    @PostMapping("/update")
    public OperationResult updateInventory(@RequestBody @Valid InventoryUpdateDto dto) {
        OperationResult result = service.publishInventoryUpdate(dto);
        return result;
    }
}