package com.bilibili.user.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(description = "合集中的所有视频信息")
public class AllVideoInEnsembleVO {
    @ApiModelProperty("总播放量")
    private long totalPlayCount;
    @ApiModelProperty("总视频数")
    private long totalVideoCount;
    @ApiModelProperty("在合集中的位置")
    private long placeInTotal;
    @ApiModelProperty("合集的简介")
    private String intro;
    @ApiModelProperty("合集中的每个视频")
    private List<VideoInEnsembleVO> videoInEnsembleVOList;

}
