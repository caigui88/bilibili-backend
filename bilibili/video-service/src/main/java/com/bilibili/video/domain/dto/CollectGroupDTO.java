package com.bilibili.video.domain.dto;

import com.bilibili.common.domain.entity.video.audience_reactions.CollectGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.beans.BeanUtils;

@Data
public class CollectGroupDTO {
    @ApiModelProperty("收藏夹名")
    private String name;
    @ApiModelProperty("所属用户的id")
    private int userId;
    @ApiModelProperty("收藏夹id")
    private Integer id;
    public CollectGroup toEntity(){
        CollectGroup danmaku=new CollectGroup();
        BeanUtils.copyProperties(this,danmaku);
        return danmaku;
    }
}
