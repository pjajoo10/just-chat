package com.just_chat.message_service.repository;

public interface ChatRepository extends JpaRepository<Chat, String>{
    boolean existsByChatId(Long chatId);
}
