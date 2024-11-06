package com.bilibili.user.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
@ApiModel("关注或粉丝列表")
public class IdolOrFansVO {
    @ApiModelProperty("用户昵称")
    private String nickname;
    @ApiModelProperty("用户id")
    private Integer userId;
    @ApiModelProperty("简介")
    private String intro;
    @ApiModelProperty("粉丝数")
    private int fansCount;
    @ApiModelProperty("关注数")
    private int idolCount;
    @ApiModelProperty("关注时间")
    private LocalDateTime createTime;
    @ApiModelProperty("头像")
    private String cover;
}
