package com.seashell.kafka_consumer.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.util.backoff.FixedBackOff;

import com.seashell.kafka_consumer.dto.InventoryBatchMessageDto;
import com.seashell.kafka_consumer.dto.InventoryMessageDto;
import com.seashell.kafka_consumer.exception.InvalidDtoException;
import com.seashell.kafka_consumer.exception.InventoryNotFoundException;
import com.seashell.kafka_consumer.exception.retrytest;

@EnableKafka
@Configuration
public class KafkaConsumerConfig {

    @Bean
    public ConsumerFactory<String, InventoryMessageDto> singleConsumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9094");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "inventory-consumer-group");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
      props.put(JsonDeserializer.VALUE_DEFAULT_TYPE, "com.seashell.kafka_consumer.dto.InventoryMessageDto");

                JsonDeserializer<InventoryMessageDto> deserializer
                = new JsonDeserializer<>();
        deserializer.addTrustedPackages("*"); // 或指定你包名
        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), deserializer);

    }

    @Bean
    public ConsumerFactory<String, InventoryBatchMessageDto> batchConsumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9094");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "inventory-consumer-group");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        props.put(JsonDeserializer.VALUE_DEFAULT_TYPE, "com.seashell.kafka_consumer.dto.InventoryBatchMessageDto");

        JsonDeserializer<InventoryBatchMessageDto> deserializer
                = new JsonDeserializer<>();
       
        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), deserializer);
     
    }

    @Bean
    public DefaultErrorHandler errorHandler() {
        // 固定間隔 2 秒，最多重試 3 次
        FixedBackOff backOff = new FixedBackOff(2000L, 3);

        DefaultErrorHandler errorHandler = new DefaultErrorHandler(
                (record, ex) -> {
                    // Recovery 行為：重試達上限、屬於 not retry 範圍 ，就去執行
                    System.err.println(
                            "Record failed after retries. Topic=" + record.topic()
                            + ", Partition=" + record.partition()
                            + ", Offset=" + record.offset()
                            + ", Key=" + record.key()
                            + ", Exception=" + ex.getMessage());
                },
                backOff);

        // 設定哪些 Exception 不重試
        errorHandler.addNotRetryableExceptions(
                InventoryNotFoundException.class,
                InvalidDtoException.class);

        // 設定哪些 Exception 需要重試
        errorHandler.addRetryableExceptions(retrytest.class);

        // 每次 retry 前可印出警告
        errorHandler.setRetryListeners((record, ex, deliveryAttempt) -> {
            Throwable actualException = ex;
            while (actualException instanceof org.springframework.kafka.listener.ListenerExecutionFailedException
                    && actualException.getCause() != null) {
                actualException = actualException.getCause();
            }

            System.out.println("Actual exception: " + actualException.getClass().getSimpleName());
            System.out.println("Message: " + actualException.getMessage());

            System.out.println(
                    "Retry attempt " + deliveryAttempt
                    + " for record key=" + record.key()
                    + ", reason=" + ex.getClass().getSimpleName());
        });

        return errorHandler;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, InventoryMessageDto> singleConsumerListenerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, InventoryMessageDto> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(singleConsumerFactory());

        // 這裡指定手動 ack。配合application裡auto commmit = false
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL);
        factory.getContainerProperties().setPollTimeout(3000);
        factory.setCommonErrorHandler(errorHandler());
        return factory;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, InventoryBatchMessageDto> batchConsumerListenerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, InventoryBatchMessageDto> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(batchConsumerFactory());

        // 這裡指定手動 ack。配合application裡auto commmit = false
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL);
        factory.getContainerProperties().setPollTimeout(3000);
        factory.setCommonErrorHandler(errorHandler());
        return factory;
    }

}
