package com.bilibili.user.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@ApiModel(description = "用户信息DTO")
public class UserInfoVO {
    @ApiModelProperty("作者id")
    public Integer id;
    @ApiModelProperty("作者的头像")
    private String cover;
    @ApiModelProperty("作者昵称")
    private String nickname;
    @ApiModelProperty("作者简介")
    private String intro;
    @ApiModelProperty("粉丝数")
    private Integer idolCount;
    @ApiModelProperty("关注数")
    private Integer fansCount;
    @ApiModelProperty("点赞量")
    private Integer likeCount;
    @ApiModelProperty("播放量")
    private Integer playCount;
    @ApiModelProperty("是否关注")
    private Boolean isFollowing;
}
