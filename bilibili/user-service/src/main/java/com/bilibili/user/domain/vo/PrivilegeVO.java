package com.bilibili.user.domain.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@ApiModel(description = "用户权限")
public class PrivilegeVO {
    Integer collectGroup;
    Integer remotelyLike;
    Integer fansList;
    Integer idolList;
}
