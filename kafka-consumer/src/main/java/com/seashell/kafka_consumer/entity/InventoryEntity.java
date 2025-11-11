package com.seashell.kafka_consumer.entity;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.Version;

@Entity
@Table(name = "inventory") // 對應資料庫的 table 名稱
public class InventoryEntity { // 代表資料表中「一筆」資料

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 每一項商品的唯一代號
    @Column(nullable = false, unique = true)
    private String productId;

    // 目前庫存數量
    @Column(nullable = false)
    private Integer quantity = 0;

    // 最近一次更新時間
    private Instant updatedAt ;

    private Instant createdAt ;

    @Version
    private long version; // 防止race condition的輔助欄位 Hibernate 自動管理，單調增加。 不須 getter settter 

     @PrePersist
    protected void onCreate() {
        Instant now = Instant.now(); // 你寫成 Insant.now() 錯了
        createdAt = now;
        updatedAt = now;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = Instant.now(); // 少了分號
    }
    public InventoryEntity(String productId, Integer quantity) {
        this.productId = productId;
        this.quantity = quantity;

    }

    public Long getId() {
        return id;
    }

    public String getProductId() {
        return productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

}
