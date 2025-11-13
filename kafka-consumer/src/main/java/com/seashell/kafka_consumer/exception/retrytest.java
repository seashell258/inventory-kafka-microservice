package com.seashell.kafka_consumer.exception;

public class retrytest extends RuntimeException {
        public retrytest(String message) {
        super(message);
    }
}
