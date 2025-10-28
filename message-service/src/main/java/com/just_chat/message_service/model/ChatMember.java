package com.just_chat.message_service.model;

@Entity
public class ChatMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private long chatId;
    private long userId;
}
