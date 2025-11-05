package com.seashell.kafka_consumer.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "inventory") // 對應資料庫的 table 名稱
public class InventoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 每一項商品的唯一代號
    @Column(nullable = false, unique = true)
    private String productId;

    // 目前庫存數量
    @Column(nullable = false)
    private Integer quantity = 0;

    // 最近一次更新時間（方便查最新資料）
    @Column(nullable = false)
    private Long lastUpdatedTimestamp;

    // 👉 可加上 constructor、getter、setter
    public InventoryEntity() {}

    public InventoryEntity(String productId, Integer quantity, Long lastUpdatedTimestamp) {
        this.productId = productId;
        this.quantity = quantity;
        this.lastUpdatedTimestamp = lastUpdatedTimestamp;
    }

    public Long getId() {
        return id;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Long getLastUpdatedTimestamp() {
        return lastUpdatedTimestamp;
    }

    public void setLastUpdatedTimestamp(Long lastUpdatedTimestamp) {
        this.lastUpdatedTimestamp = lastUpdatedTimestamp;
    }
}
