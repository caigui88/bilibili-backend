package com.bilibili.video.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DeleteHistoryVideoDTO {
    @ApiModelProperty("删除一条播放记录对应的视频id")
    Integer videoId;
    @ApiModelProperty("该播放记录对应用户id")
    Integer userId;

}
