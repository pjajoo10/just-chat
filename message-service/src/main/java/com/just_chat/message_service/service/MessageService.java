package just_chat.message_service.service;

public class MessageService {

    //validate payload
    validateMessage(MessageRequest dto){
        //is the sender_id valid: is it registered in the user database
        //is the chat_id valid: does it already exist?
        //is the message valid: is it text? 
    }

    //find recipients of the chat_id
    findRecipients(MessageRequest dto){

        //database operation on findusers(dto.chat_id)

    }
    //forward to online recipients 
    //how to check if they are online?: in v3, userId->socket mapping was used
    //return to sender if any recipient is offline
    
}
