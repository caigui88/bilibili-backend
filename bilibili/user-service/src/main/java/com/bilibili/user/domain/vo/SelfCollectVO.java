package com.bilibili.user.domain.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@ApiModel(description = "个人收藏夹信息")
public class SelfCollectVO {
    private Integer videoId;
    private String videoCover;
    private Integer authorId;
    private String authorName;
    private String videoName;
    private LocalDateTime createTime;
}
