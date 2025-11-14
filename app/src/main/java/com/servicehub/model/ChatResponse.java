package com.servicehub.model;

public class ChatResponse {
    private final String reply;
    private final String method;

    public ChatResponse(String reply, String method) {
        this.reply = reply;
        this.method = method;
    }

    public String getReply() {
        return reply;
    }

    public String getMethod() {
        return method;
    }
}
