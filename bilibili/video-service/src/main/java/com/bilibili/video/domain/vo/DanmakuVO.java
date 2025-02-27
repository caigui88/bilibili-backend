package com.bilibili.video.domain.vo;

import com.bilibili.common.domain.entity.video.audience_reactions.Danmaku;
import com.bilibili.common.serializer.RemoveTSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.time.LocalDateTime;

@Data
public class DanmakuVO {
    @ApiModelProperty("发送弹幕的用户的id")
    private int userId;
    @ApiModelProperty("弹幕的内容")
    private String content;
    @ApiModelProperty("发送弹幕时视频所处于的位置")
    private String place;
    @JsonSerialize(using = RemoveTSerializer.class)
    @ApiModelProperty("弹幕什么时候发送的")
    private LocalDateTime createTime;
    @ApiModelProperty("所在视频id")
    private String videoId;

    public DanmakuVO(Danmaku danmaku){
        BeanUtils.copyProperties(danmaku,this);
    }
}
