package com.seashell.kafka_consumer.dto;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

public class InventoryBatchMessageDto {
    @NotEmpty
    @Valid  
private List<InventoryMessageDto> updates;

    public List<InventoryMessageDto> getUpdates() {
        return updates;
    }

    public void setUpdates(List<InventoryMessageDto> updates) {
        this.updates = updates;
    }

}
