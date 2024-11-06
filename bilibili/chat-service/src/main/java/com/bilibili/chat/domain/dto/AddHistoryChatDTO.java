package com.bilibili.chat.domain.dto;

import com.bilibili.common.domain.entity.chat.Chat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.beans.BeanUtils;

@Data
public class AddHistoryChatDTO {
    @ApiModelProperty("发送消息人的id")
    private int senderId;
    @ApiModelProperty("接受消息人的id")
    private int receiverId;
    @ApiModelProperty("消息内容")
    private String content;
    @ApiModelProperty("聊天会话的id")
    private Integer sessionId;
    public Chat toEntity(){
        Chat chat=new Chat();
        BeanUtils.copyProperties(this,chat);
        return chat;
    }
}
