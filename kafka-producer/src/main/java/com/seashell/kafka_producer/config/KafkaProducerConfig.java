package com.seashell.kafka_producer.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import com.seashell.kafka_producer.dto.InventoryUpdateBatchDto;
import com.seashell.kafka_producer.dto.InventoryUpdateDto;

@Configuration
public class KafkaProducerConfig {

    @Bean
    public ProducerFactory<String, InventoryUpdateDto> producerFactory() {
        Map<String, Object> config = new HashMap<>();
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9094");
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class); // 改成 StringSerializer

        return new DefaultKafkaProducerFactory<>(config);

    }

    @Bean
    public KafkaTemplate<String, InventoryUpdateDto> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

        @Bean
    public ProducerFactory<String, InventoryUpdateBatchDto> producerBatchFactory() {
        Map<String, Object> config = new HashMap<>();
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9094");
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class); // 改成 StringSerializer

        return new DefaultKafkaProducerFactory<>(config);

    }

        @Bean
    public KafkaTemplate<String, InventoryUpdateBatchDto> kafkaBatchTemplate() {
        return new KafkaTemplate<>(producerBatchFactory());
    }
}
