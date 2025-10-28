package com.just_chat.message_service.repository;

public interface ChatMemberRepository extends JpaRepository<ChatMember, Long> {
    boolean existsByUserIdAndChatId(Long userId, Long chatId);
    @Query("SELECT cm.userId FROM ChatMember cm WHERE cm.chatId = :chatId")
    List<ChatMember> findUserIdsByChatId(@Param("chatId") Long chatId);
}
