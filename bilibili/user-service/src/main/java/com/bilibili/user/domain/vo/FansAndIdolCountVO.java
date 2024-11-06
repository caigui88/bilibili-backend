package com.bilibili.user.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@ApiModel("粉丝和关注数")
@AllArgsConstructor
public class FansAndIdolCountVO {
    @ApiModelProperty("粉丝数")
    int fansCount;
    @ApiModelProperty("关注数")
    int idolCount;
}
