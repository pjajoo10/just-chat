package com.just_chat.message_service.dto;

public class MessageRequest {

    private String sender_id;
    private String chat_id;
    private String content;
    
    public String getChatId() {
        return chat_id;
    }

    public String getSenderId() {
        return sender_id;
    }

    public String getContent() {
        return content;
    }
}
