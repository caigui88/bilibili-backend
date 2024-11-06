package com.bilibili.video.domain.dto;

import com.bilibili.common.domain.entity.video.audience_reactions.Danmaku;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.beans.BeanUtils;

@Data
public class AddDanmakuDTO {
    @ApiModelProperty("发布评论的用户id,如果要删除弹幕的话可以用到")
    private int userId;
    @ApiModelProperty("弹幕内容")
    private String content;
    @ApiModelProperty("弹幕所在位置")
    private String place;
    @ApiModelProperty("所在视频")
    public int videoId;
    public Danmaku toEntity(){
        Danmaku danmaku=new Danmaku();
        BeanUtils.copyProperties(this,danmaku);
        return danmaku;
    }
}
