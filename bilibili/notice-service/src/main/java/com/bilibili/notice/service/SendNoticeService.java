package com.bilibili.notice.service;

import com.bilibili.common.domain.entity.chat.Chat;
import com.bilibili.common.domain.entity.notice.CommentNotice;
import com.bilibili.common.domain.entity.notice.Dynamic;
import com.bilibili.common.domain.pojo.LikeNoticeAddOrDelete;
import com.bilibili.common.domain.pojo.UploadVideo;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface SendNoticeService {
    Boolean sendVideoUpdateMessageWithCallback(Dynamic dynamic) throws JsonProcessingException;
    Boolean sendVideoLikeMessageWithCallback(LikeNoticeAddOrDelete likeNotice) throws JsonProcessingException;
    Boolean sendCommentMessageWithCallback(CommentNotice commentNotice) throws JsonProcessingException;
    Boolean sendChatMessageWithCallback(Chat chat) throws JsonProcessingException;
    Boolean sendUploadNotice(UploadVideo uploadVideo)throws JsonProcessingException;
}
