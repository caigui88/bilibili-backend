package com.bilibili.notice.controller;

import com.bilibili.common.domain.entity.chat.Chat;
import com.bilibili.common.domain.entity.notice.CommentNotice;
import com.bilibili.common.domain.entity.notice.Dynamic;
import com.bilibili.common.domain.pojo.LikeNoticeAddOrDelete;
import com.bilibili.common.domain.pojo.UploadVideo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.bilibili.notice.service.SendNoticeService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;

@RestController
@RequestMapping("/notice")

public class SendNoticeController {
    @Resource
    SendNoticeService sendNoticeService;
    @PostMapping("/sendDynamicNotice")
    @ApiIgnore
    public Boolean dynamicNotice(@RequestBody Dynamic dynamic) throws JsonProcessingException {
        sendNoticeService.sendVideoUpdateMessageWithCallback(dynamic);
        return true;
    }
    @PostMapping("/sendLikeNotice")
    @ApiIgnore
    public void videolikeNotice(@RequestBody LikeNoticeAddOrDelete likeNotice) throws JsonProcessingException {
        sendNoticeService.sendVideoLikeMessageWithCallback(likeNotice);
    }
    @ApiIgnore
    @PostMapping("/sendCommentNotice")
    public void commentNotice(@RequestBody CommentNotice commentNotice) throws JsonProcessingException {
        sendNoticeService.sendCommentMessageWithCallback(commentNotice);
    }
    @ApiIgnore
    @PostMapping("/sendChatNotice")
    public void chatNotice(@RequestBody Chat chat) throws JsonProcessingException {
        sendNoticeService.sendChatMessageWithCallback(chat);
    }
    @ApiIgnore
    @PostMapping("/sendUploadNotice")
    public Boolean sendUploadNotice(@RequestBody UploadVideo uploadVideo) throws JsonProcessingException {
        return sendNoticeService.sendUploadNotice(uploadVideo);
    }

}
