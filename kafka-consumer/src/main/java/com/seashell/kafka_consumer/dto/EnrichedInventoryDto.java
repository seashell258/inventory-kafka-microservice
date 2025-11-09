package com.seashell.kafka_consumer.dto;

import lombok.Builder;

/**
 * 入庫用 DTO（已補齊資料）
 */
@Builder
public class EnrichedInventoryDto {

    // 對應 InventoryEntity 的 productId. for Both InventoryLog and Inventory 
    private String productId;

    // 舊庫存（查庫得到的原值） for InventoryLog 
    private Integer oldQuantity;

    // 新庫存（計算後的新值）  for Both InventoryLog and Inventory 
    private Integer newQuantity;

    // 變動量（ message dto 的輸入） for InventoryLog  
    private Integer quantityChange;

    // for InventoryLog. inventory 本身則用@preUpdate得到程式自動生成的 last updated time
    private Long eventTime;

    // 可選備註欄位（例如變更原因）
    private String changeReason;


    public EnrichedInventoryDto(String productId, Integer oldQuantity, Integer newQuantity,
                                Integer quantityChange, Long eventTime, String changeReason) {
        this.productId = productId;
        this.oldQuantity = oldQuantity;
        this.newQuantity = newQuantity;
        this.quantityChange = quantityChange;
        this.eventTime = eventTime;
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

    public Long getEventTime() {
        return eventTime;
    }

    public void setEventTime(Long eventTime) {
        this.eventTime = eventTime;
    }

    public String getChangeReason() {
        return changeReason;
    }

    public void setChangeReason(String changeReason) {
        this.changeReason = changeReason;
    }
}
