package com.bilibili.chat.domain.dto;

import com.bilibili.common.domain.entity.chat.Chat;
import com.bilibili.common.domain.entity.chat.ChatSession;
import lombok.Data;
import org.springframework.beans.BeanUtils;

@Data
public class ChatSessionDTO {
    Integer senderId;
    Integer receiverId;
    String updateContent;
    public ChatSession toSessionEntity(){
        ChatSession chatSession=new ChatSession();
        BeanUtils.copyProperties(this,chatSession);
        return chatSession;
    }
    public Chat toChatEntity(){
        Chat chat=new Chat();
        BeanUtils.copyProperties(this,chat);
        return chat;
    }
}
