package com.servicehub.model;

public class ChatRequest {
    private final String message;

    public ChatRequest(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
