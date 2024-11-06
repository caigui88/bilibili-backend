package com.bilibili.user.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "某个视频在合集中的信息")
public class VideoInEnsembleVO {
    @ApiModelProperty("视频id")
    private Integer videoId;
    @ApiModelProperty("视频名")
    private String videoName;
    @ApiModelProperty("视频时长")
    private String length;
    @ApiModelProperty("合集名称")
    private String ensembleName;
    @ApiModelProperty("合集id，方便跳转")
    private Integer ensembleId;
}
