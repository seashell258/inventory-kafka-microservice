package com.seashell.kafka_consumer.entity;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "inventory_log")
public class InventoryLogEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String productId;

    @Column(nullable = false)
    private Integer oldQuantity;

    @Column(nullable = false)
    private Integer quantityChange;

    @Column(nullable = false)
    private Integer newQuantity;

    @Column(nullable = false)
    private String updatedBy;

    @Column(nullable = false, updatable = false)
    private Instant updatedAt;

    @Column
    private String changeReason; // 庫存變動原因 可選擇性填入


    // ===== Constructor =====
    public InventoryLogEntity() {
        this.updatedAt = Instant.now(); // 無參構造自動填時間
    }

    public InventoryLogEntity(String productId, Integer oldQuantity, Integer quantityChange,
            Integer newQuantity, String updatedBy, String changeReason) {
        this.productId = productId;
        this.oldQuantity = oldQuantity;
        this.quantityChange = quantityChange;
        this.newQuantity = newQuantity;
        this.updatedBy = updatedBy;
        this.changeReason = changeReason;
        this.updatedAt = Instant.now(); // 建構時自動填時間
    }

    // ===== Getter / Setter =====
    public Long getId() {
        return id;
    }

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

    public Integer getQuantityChange() {
        return quantityChange;
    }

    public void setQuantityChange(Integer quantityChange) {
        this.quantityChange = quantityChange;
    }

    public Integer getNewQuantity() {
        return newQuantity;
    }

    public void setNewQuantity(Integer newQuantity) {
        this.newQuantity = newQuantity;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getChangeReason() {
        return changeReason;
    }

    public void setChangeReason(String changeReason) {
        this.changeReason = changeReason;
    }

}
