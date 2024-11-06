package com.bilibili.user.domain.dto;

import com.bilibili.common.domain.entity.user.Privilege;
import lombok.Data;
import org.springframework.beans.BeanUtils;

@Data
public class EditUserCenterPrivilegeDTO {
    int userId;
    int collectGroup;
    int remotelyLike;
    int fansList;
    int idolList;
    public Privilege toEntity(){
        Privilege privilege=new Privilege();
        BeanUtils.copyProperties(this,privilege);
        return privilege;
    }
}
