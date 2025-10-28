package com.just_chat.message_service.model;

@Entity
public class Message {
    @Id
    private long messageId;
    private long chatId;
    private long senderId;
    private string content;
}
