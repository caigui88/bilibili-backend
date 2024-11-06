package com.bilibili.user.domain.vo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@ApiModel(description = "个人中心内容")
public class SelfCenterContentVO {
    @ApiModelProperty("投稿的视频")
    private List<SelfVideoVO> selfVideoVOList;
    @ApiModelProperty("收藏的视频")
    private List<SelfCollectVO> selfCollectVOList;
    @ApiModelProperty("最近点赞视频")
    private List<RecentlyLikeVideoVO> recentlyLikeVideoVOList;
    @ApiModelProperty("粉丝列表")
    private List<IdolOrFansVO> fansVOList =new ArrayList<>();
    @ApiModelProperty("关注列表")
    private List<IdolOrFansVO> idolVOList =new ArrayList<>();
}
