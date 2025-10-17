package com.just_chat.message_service.connection;

public class ChatWebSocketHandler extends TextWebSocketHandler{
    
    private final MessageService messageService;


    //methods

    public afterConnectionEstablished(WebSocketSession session){

    }

    public handleTextMessage(WebSocketSession session, TextMessage message){

        //messageService.validateMessage(session, message);



    }

    public afterConnectionClosed(WesSocketSession session, CloseStatus status){

    }
}
