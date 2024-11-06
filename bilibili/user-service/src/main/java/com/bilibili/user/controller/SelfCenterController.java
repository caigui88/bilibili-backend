package com.bilibili.user.controller;

import com.bilibili.common.util.Result;
import com.bilibili.user.domain.dto.EditUserCenterPrivilegeDTO;
import com.bilibili.user.domain.vo.PrivilegeVO;
import com.bilibili.user.domain.vo.SelfCenterContentVO;
import com.bilibili.user.service.impl.SelfCenterServiceImpl;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Slf4j
@RequestMapping("/selfCenter")
public class SelfCenterController {
    @Resource
    SelfCenterServiceImpl selfCenterService;
    @ApiOperation("修改用户个人主页各模块权限开放状态")
    @PostMapping("/editUserPrivilege")
    public Result<Boolean> editUserPrivilege(@RequestBody EditUserCenterPrivilegeDTO editUserCenterPrivilegeDTO){
        log.info("1");
        return selfCenterService.editUserPrivilege(editUserCenterPrivilegeDTO);

    }
    @ApiOperation("获取用户个人主页各模块权限开放状态")
    @GetMapping("/getUserPrivilege/{userId}")
    public Result<PrivilegeVO> getUserPrivilege(@PathVariable Integer userId){
        return selfCenterService.getUserPrivilege(userId);
    }

    @GetMapping("/getPersonalCenterContent/{selfId}/{visitedId}")
    @ApiOperation("获取个人主页内容")
    public Result<SelfCenterContentVO> getPersonalVideo(@PathVariable Integer selfId, @PathVariable Integer visitedId){
        log.info("1");
        return selfCenterService.getPersonalCenterContent(selfId,visitedId);
    }
}
