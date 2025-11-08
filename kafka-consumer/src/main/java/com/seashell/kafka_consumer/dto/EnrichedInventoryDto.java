package com.seashell.kafka_consumer.dto;

import lombok.Builder;

/**
 * 入庫用 DTO（已補齊資料）
 */
@Builder
public class EnrichedInventoryDto {

    // 對應 InventoryEntity 的 productId
    private String productId;

    // 舊庫存（查庫得到的原值）
    private Integer oldQuantity;

    // 新庫存（計算後的新值）
    private Integer newQuantity;

    // 變動量（可以從 message dto 計算得出）
    private Integer quantityChange;

    // 對應 InventoryEntity 的 lastUpdatedTimestamp（建議用毫秒）
    private Long lastUpdatedTimestamp;

    // 可選備註欄位（例如變更原因）
    private String changeReason;


    public EnrichedInventoryDto(String productId, Integer oldQuantity, Integer newQuantity,
                                Integer quantityChange, Long lastUpdatedTimestamp, String changeReason) {
        this.productId = productId;
        this.oldQuantity = oldQuantity;
        this.newQuantity = newQuantity;
        this.quantityChange = quantityChange;
        this.lastUpdatedTimestamp = lastUpdatedTimestamp;
        this.changeReason = changeReason;
    }

    // -------- getter & setter --------

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public Integer getOldQuantity() {
        return oldQuantity;
    }

    public void setOldQuantity(Integer oldQuantity) {
        this.oldQuantity = oldQuantity;
    }

    public Integer getNewQuantity() {
        return newQuantity;
    }

    public void setNewQuantity(Integer newQuantity) {
        this.newQuantity = newQuantity;
    }

    public Integer getQuantityChange() {
        return quantityChange;
    }

    public void setQuantityChange(Integer quantityChange) {
        this.quantityChange = quantityChange;
    }

    public Long getLastUpdatedTimestamp() {
        return lastUpdatedTimestamp;
    }

    public void setLastUpdatedTimestamp(Long lastUpdatedTimestamp) {
        this.lastUpdatedTimestamp = lastUpdatedTimestamp;
    }

    public String getChangeReason() {
        return changeReason;
    }

    public void setChangeReason(String changeReason) {
        this.changeReason = changeReason;
    }
}
