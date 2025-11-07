package com.seashell.kafka_consumer.dto;

import java.time.Instant;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class InventoryMessageDTO {

    @NotBlank
    private String productId;      // 商品編號

    @Min(-1000)
    @Max(1000)
    private int quantityChange;    // 庫存增減（負數代表出貨）

    @NotBlank
    private String updatedBy;      // 更改的組織為何

    private String changeReason;  //庫存變更原因選填

    private Instant eventTime;  //庫存變更原因選填。 選 instant 而不是 localDateTime 方便跨時區的系統使用。

    // ===== Getter / Setter =====
    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public int getQuantityChange() {
        return quantityChange;
    }

    public void setQuantityChange(int quantityChange) {
        this.quantityChange = quantityChange;
    }

    public String getChangeReason() {
        return changeReason;
    }

    public void setChangeReason(String changeReason) {
        this.changeReason = changeReason;
    }

    public Instant getEventTime() {
        return eventTime;
    }

    public void setEventTime(Instant eventTime) {
        this.eventTime = eventTime;
    }

}
