package com.bilibili.video.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
@Data
public class CommentVO {
    @ApiModelProperty("顶级评论")
    public TopCommentVO topCommentVO =new TopCommentVO();
    @ApiModelProperty("后续层评论")
    public List<CommentDetailVO> commentDetailRespons =new ArrayList<>();
}
