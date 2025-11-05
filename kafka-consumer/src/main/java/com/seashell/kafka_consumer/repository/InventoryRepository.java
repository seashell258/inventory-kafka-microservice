package com.seashell.kafka_consumer.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.seashell.kafka_consumer.entity.InventoryEntity;

public interface InventoryRepository extends JpaRepository<InventoryEntity, Long> {

    Optional<InventoryEntity> findByProductId(String productId);
    // 可以加自訂查詢，格式findBy + <Entity屬性名首字母大寫> 
}
