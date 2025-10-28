package com.just_chat.message_service.controller;

public class ChatWebSocketHandler extends TextWebSocketHandler{
    
    private final MessageService messageService;


    //methods

    public afterConnectionEstablished(WebSocketSession session){

        

    }

    public handleTextMessage(WebSocketSession session, TextMessage message){
        String jsonPayload=message.getPayload();
        MessageRequest dtoPayload = objectMapper.readValue(jsonPayload, MessageRequest.class);
        messageService.processMessage(session, dtoPayload);
    }

    public afterConnectionClosed(WesSocketSession session, CloseStatus status){

    }
}
