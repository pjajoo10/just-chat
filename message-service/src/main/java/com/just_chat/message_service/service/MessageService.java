package just_chat.message_service.service;

public class MessageService {

    private ChatRepository chatRepository;
    private ChatMemberRepository chatMemberRepository;


    processMessage(WebsocketSession session, MessageRequest payload){
        //validate
        if(!validateMessage(payload)){
            return;
        }
        //Collect recipients list
        List<String> chatParticipants = findRecipients(payload.getChatId());

        //forward to online ones
        
        //send feedback to sender    
        }

    //validate payload
    bool validateMessage(MessageRequest payload){
        //is the sender_id valid: is it registered in the user database
        //is the chat_id valid: does it already exist?
        //is the sender_id part of the given chat_id | user cannot send message to chat they dont belong?
        //is the message valid: is it text? 
        return (chatRepository.existsByChatId(payload.getChatId()) && chatMemberRepository.existsByUserIdAndChatid(payload.getUserId(),payload.getChatId()));
    }

    //find recipients of the chat_id
    List<String> findRecipients(String chatId){

        //gather user ids through database operation, findusers(dto.chat_id)
        List<String> chatParticipants = chatMemberRepository.findUserIdsByChatId(chatId);
        return chatParticipants;
    }
    //forward to online recipients 
    //how to check if they are online?: in v3, userId->socket mapping was used
    //return to sender if any recipient is offline
    
}
