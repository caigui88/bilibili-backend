package com.bilibili.user.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@ApiModel(description = "最近喜欢的视频")
public class RecentlyLikeVideoVO {
    @ApiModelProperty("视频id")
    private int videoId;
    @ApiModelProperty("视频名字")
    private String name;
    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;
    @ApiModelProperty("视频时长")
    private String length;
    @ApiModelProperty("视频播放量")
    private int playCount;
    @ApiModelProperty("弹幕量")
    private int danmakuCount;
    @ApiModelProperty("视频封面")
    private String cover;
}
