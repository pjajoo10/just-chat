package com.just_chat.message_service.config.WebSocketConfig;

public class WebSocketConfig implements WebSocketConfigurer {
    
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry){

        registry.addHandler(new ChatWebSocketController(), "/ws/chat").setAllowedOrigins("*");

    }
}
