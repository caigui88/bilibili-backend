package com.bilibili.chat.domain.vo;
import com.bilibili.common.domain.entity.chat.Chat;
import com.bilibili.common.serializer.RemoveTSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.time.LocalDateTime;

@Data
public class HistoryChatVO {
    @ApiModelProperty("聊天的内容")
    private String content;
    @ApiModelProperty("这条消息的发送者id")
    private Integer senderId;
    @JsonSerialize(using = RemoveTSerializer.class)
    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;
    @ApiModelProperty("该条聊天记录的id")
    private Integer id;
    @ApiModelProperty("该条记录的已读和未读状态")
    private Integer status;
    public HistoryChatVO(Chat chat){
        BeanUtils.copyProperties(chat,this);
    }
}
