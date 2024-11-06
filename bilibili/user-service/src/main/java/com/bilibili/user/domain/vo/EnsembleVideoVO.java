package com.bilibili.user.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@ApiModel(description = "合集中某个视频的信息")
public class EnsembleVideoVO {
    @ApiModelProperty("视频id")
    private Integer videoId;
    @ApiModelProperty("视频名字")
    private String name;
    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;
    @ApiModelProperty("视频时长")
    private String length;
    @ApiModelProperty("视频播放量")
    private Integer playCount;
    @ApiModelProperty("所属合集id")
    private Integer ensembleId;
    @ApiModelProperty("视频封面")
    private String cover;
}
