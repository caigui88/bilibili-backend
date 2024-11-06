package com.bilibili.video.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class CollectDTO {
    @ApiModelProperty("收藏的视频的id")
    private Integer videoId;
    @ApiModelProperty("该用户的收藏夹的id")
    private Integer collectGroupId;
    @ApiModelProperty("操作类型,true是收藏，false是取消收藏")
    private Boolean type;
}
