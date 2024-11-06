package com.bilibili.video.domain.vo;

import com.bilibili.common.serializer.RelativeTimeSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class HistoryVideoVO {
    @ApiModelProperty("视频的名字")
    private String videoName;
    @JsonSerialize(using = RelativeTimeSerializer.class)
    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;
    @ApiModelProperty("视频时长")
    private String length;
    @ApiModelProperty("封面的url")
    private String videoCover;
    @ApiModelProperty("视频id，方便跳转到视频详情页")
    private Integer videoId;
    @ApiModelProperty("作者名")
    private String authorName;
    @ApiModelProperty("作者id")
    private Integer authorId;
    @ApiModelProperty("作者头像")
    private String authorCover;
}
