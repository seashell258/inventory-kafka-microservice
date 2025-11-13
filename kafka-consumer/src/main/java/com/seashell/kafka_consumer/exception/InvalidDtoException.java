package com.seashell.kafka_consumer.exception;

public class InvalidDtoException extends RuntimeException {

    private final Object invalidDto; // 可選，存原 DTO，方便 recovery/log
    private final String details;    // 驗證訊息摘要

    public InvalidDtoException(String details) {
        super(details);
        this.invalidDto = null;
        this.details = details;
    }

    public InvalidDtoException(String details, Object invalidDto) {
        super(details);
        this.invalidDto = invalidDto;
        this.details = details;
    }

    public Object getInvalidDto() {
        return invalidDto;
    }

    public String getDetails() {
        return details;
    }
}