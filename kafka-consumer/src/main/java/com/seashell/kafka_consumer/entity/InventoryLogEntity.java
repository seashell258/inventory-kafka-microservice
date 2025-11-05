package com.seashell.kafka_consumer.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "inventory_log")
public class InventoryLogEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 對應哪一個商品
    @Column(nullable = false)
    private String productId;

    // 增加或減少的數量（例如 +5, -3）
    @Column(nullable = false)
    private Integer quantityChange;

    // 操作來源（訂單、手動、補貨）
    private String sourceType;

    // 操作的時間戳
    @Column(nullable = false)
    private Long timestamp;

    public InventoryLogEntity() {}

    public InventoryLogEntity(String productId, Integer quantityChange, String sourceType, Long timestamp) {
        this.productId = productId;
        this.quantityChange = quantityChange;
        this.sourceType = sourceType;
        this.timestamp = timestamp;
    }

    // Getter / Setter
    public Long getId() {
        return id;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public Integer getQuantityChange() {
        return quantityChange;
    }

    public void setQuantityChange(Integer quantityChange) {
        this.quantityChange = quantityChange;
    }

    public String getSourceType() {
        return sourceType;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
}
