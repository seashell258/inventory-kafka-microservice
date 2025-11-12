package com.seashell.kafka_consumer.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.seashell.kafka_consumer.entity.InventoryEntity;

import jakarta.persistence.LockModeType;

public interface InventoryRepository extends JpaRepository<InventoryEntity, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT i FROM InventoryEntity i WHERE i.productId = :productId")
    Optional<InventoryEntity> findByProductIdForUpdate(@Param("productId") String productId);
     

    Optional<InventoryEntity> findByProductId(String productId);
}
