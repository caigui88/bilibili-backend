package com.bilibili.chat.service;

import com.bilibili.chat.domain.dto.ChatSessionDTO;
import com.bilibili.chat.domain.vo.PPTVO;
import com.bilibili.chat.domain.dto.ChangeChatStatusDTO;
import com.bilibili.chat.domain.vo.ChatSessionVO;
import com.bilibili.chat.domain.vo.HistoryChatVO;
import com.bilibili.chat.domain.vo.TempSessionVO;
import com.bilibili.common.util.Result;

import java.io.IOException;
import java.util.List;

public interface ChatService {
    Result<Boolean> changeChatStatus(ChangeChatStatusDTO changeChatStatusDTO);

    Result<Boolean> changeChatSessionTime(ChatSessionDTO chatSessionDTO);

    Result<List<ChatSessionVO>> getHistoryChatSession(Integer userId);

    Result<Boolean> addChatSessionAndContent(ChatSessionDTO chatSessionDTO);

    Result<List<HistoryChatVO>> getHistoryChat(Integer userId, Integer receiverId);


    Result<TempSessionVO> createTempSession(Integer receiverId);
    PPTVO getPPT(String describe) throws IOException;
     String getImage( String text) throws Exception;
}
