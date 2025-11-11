package com.seashell.kafka_producer.dto;

import java.util.List;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

public class InventoryUpdateBatchDto {
    @NotEmpty
    @Valid
    private List<InventoryUpdateDto> updates;

    public List<InventoryUpdateDto> getUpdates() {
        return updates;
    }

    public void setUpdates(List<InventoryUpdateDto> updates) {
        this.updates = updates;
    }
}
