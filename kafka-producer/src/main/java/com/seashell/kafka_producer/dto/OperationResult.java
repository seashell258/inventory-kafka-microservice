package com.seashell.kafka_producer.dto;


public class OperationResult {
    private final boolean success;
    private final String message;

    public OperationResult(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    // getter / setter
    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }

    public static OperationResult success(String msg) { //OperationResult 是回傳型別
        return new OperationResult(true, msg);
    }

    public static OperationResult failure(String msg) {
        return new OperationResult(false, msg);
    }
}

