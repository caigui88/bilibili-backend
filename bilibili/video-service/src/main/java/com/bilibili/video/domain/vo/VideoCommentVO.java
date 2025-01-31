package com.bilibili.video.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;
@Data
@Accessors(chain = true)
public class VideoCommentVO {
    @ApiModelProperty("评论总数")
    private long commentCount;
    @ApiModelProperty("每个评论个体")
    private List<CommentVO> commentVOList;
}
