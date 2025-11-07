package com.seashell.kafka_consumer.repository;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;


import com.seashell.kafka_consumer.entity.InventoryEntity;
import com.seashell.kafka_consumer.entity.InventoryLogEntity;


public interface InventoryLogRepository extends JpaRepository<InventoryEntity, Long> {


    Optional<InventoryLogEntity> findByProductId(String productId);
// 可以加自訂查詢，格式findBy + <Entity屬性名首字母大寫> 

}
