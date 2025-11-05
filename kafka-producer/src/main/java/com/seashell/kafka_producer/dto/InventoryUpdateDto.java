package com.seashell.kafka_producer.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

import jakarta.validation.constraints.NotBlank;

public class InventoryUpdateDto {
    @NotBlank
    private String productId;  // 商品編號
    
    @Min(-1000)
    @Max(1000)
    private int quantityChange; // 庫存增減（負數代表出貨）

    // getter + setter
    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public int getQuantityChange() {
        return quantityChange;
    }

    public void setQuantityChange(int quantityChange) {
        this.quantityChange = quantityChange;
    }
}
