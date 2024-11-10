package com.bilibili.chat.controller;

import com.bilibili.chat.domain.dto.ChangeChatStatusDTO;
import com.bilibili.chat.domain.dto.ChatSessionDTO;
import com.bilibili.chat.domain.vo.PPTVO;
import com.bilibili.chat.service.ChatService;
import com.bilibili.chat.domain.vo.ChatSessionVO;
import com.bilibili.chat.domain.vo.HistoryChatVO;
import com.bilibili.chat.domain.vo.TempSessionVO;
import com.bilibili.common.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/chat")
@Api(tags = "获取历史聊天记录和聊天对象列表")
@Slf4j
public class ChatController {
    @Resource
    ChatService chatService;

    @GetMapping("/getPPT/{describe}")
    public PPTVO getPPT(@PathVariable String describe) throws IOException, InterruptedException {
        return chatService.getPPT(describe);
    }

    @GetMapping("/getImage/{text}")
    public String getImage(@PathVariable String text) throws Exception {
        return chatService.getImage(text);
    }

    @GetMapping("/createTempSession/{receiverId}")
    @ApiOperation("创建临时会话")
    public Result<TempSessionVO> createTempSession(@PathVariable Integer receiverId) {
        return chatService.createTempSession(receiverId);
    }

    @GetMapping("/getHistoryChat/{userId}/{receiverId}")
    @ApiOperation("获取历史聊天记录")
    public Result<List<HistoryChatVO>> getHistoryChat(@PathVariable Integer userId, @PathVariable Integer receiverId) {

        return chatService.getHistoryChat(userId, receiverId);
    }

    @ApiOperation("获取历史聊天会话列表")
    @GetMapping("/getHistoryChatSession/{userId}")
    public Result<List<ChatSessionVO>> getHistoryChatSession(@PathVariable Integer userId) {

        return chatService.getHistoryChatSession(userId);
    }

    @ApiOperation("修改聊天记录的状态从未读到已读")
    @PostMapping("/changeChatStatus")
    public Result<Boolean> changeChatStatus(@RequestBody ChangeChatStatusDTO changeChatStatusDTO) {

        return chatService.changeChatStatus(changeChatStatusDTO);
    }

    @ApiOperation("修改聊天会话的最后聊天时间和最后聊天内容")
    @PostMapping("/changeChatSessionTime")
    public Result<Boolean> changeChatSessionTime(@RequestBody ChatSessionDTO chatSessionDTO) {

        return chatService.changeChatSessionTime(chatSessionDTO);
    }

    @ApiOperation("/新增聊天会话和聊天内容")
    @PostMapping("/addChatSessionAndContent")
    public Result<Boolean> addChatSessionAndContent(@RequestBody ChatSessionDTO chatSessionDTO) {

        return chatService.addChatSessionAndContent(chatSessionDTO);
    }
}
