package com.just_chat.message_service.model;

@entity
public class Message {
    private long message_id;
    private long chat_id;
    private long sender_id;
    private string text;
}
