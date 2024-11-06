package com.bilibili.user.controller;

import com.bilibili.common.util.Result;
import com.bilibili.user.domain.dto.FollowDTO;
import com.bilibili.user.service.impl.FollowServiceImpl;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;

@Slf4j
@RequestMapping("/follow")
public class FollowController {
    @Resource
    FollowServiceImpl followService;
    @PostMapping
    @ApiOperation("关注用到，自己的id和要关注的用户的id")
    public Result<Boolean> follow(@RequestBody FollowDTO followDTO) {
        log.info("1");
        return followService.follow(followDTO);
    }

    @ApiOperation("取关用到自己的id和要取消关注的用户的id")
    @PostMapping("/recallFollow")
    public Result<Boolean> recallFollow(@RequestBody FollowDTO followDTO) {
        log.info("1");
        return followService.recallFollow(followDTO);
    }
}
