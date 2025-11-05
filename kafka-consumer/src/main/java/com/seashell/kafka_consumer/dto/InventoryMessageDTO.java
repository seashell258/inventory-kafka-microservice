package com.seashell.kafka_consumer.dto;

import jakarta.validation.constraints.Min;

import jakarta.validation.constraints.NotBlank;

public class InventoryMessageDTO {

    @NotBlank(message = "productId cannot be blank")
    private String productId;

    @Min(value = 0, message = "quantity must be >= 0")
    private int quantity;

    // Constructor, Getter & Setter
    public InventoryMessageDTO() {}

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
