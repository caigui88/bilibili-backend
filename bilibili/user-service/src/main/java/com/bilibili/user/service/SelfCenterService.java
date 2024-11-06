package com.bilibili.user.service;


import com.bilibili.user.domain.vo.PrivilegeVO;
import com.bilibili.user.domain.vo.SelfCenterContentVO;
import com.bilibili.user.domain.dto.EditUserCenterPrivilegeDTO;
import com.bilibili.common.util.Result;

public interface SelfCenterService {
    Result<SelfCenterContentVO> getPersonalCenterContent(Integer selfId, Integer visitedId);
    Result<Boolean> editUserPrivilege(EditUserCenterPrivilegeDTO editUserCenterPrivilegeDTO);
    Result<PrivilegeVO> getUserPrivilege(Integer userId);
}
