package com.bilibili.chat.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ChangeChatStatusDTO {
    @ApiModelProperty("用户id")
    private Integer userId;
    @ApiModelProperty("聊天会话另一头的用户id")
    private Integer receiverId;
}
