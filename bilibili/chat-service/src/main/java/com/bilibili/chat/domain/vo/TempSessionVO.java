package com.bilibili.chat.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class TempSessionVO {
    @ApiModelProperty("昵称")
    private String nickName;
    @ApiModelProperty("头像")
    private String cover;
}
