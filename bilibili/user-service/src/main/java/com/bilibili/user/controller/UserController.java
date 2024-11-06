package com.bilibili.user.controller;


import com.bilibili.user.domain.vo.UserInfoVO;
import com.bilibili.user.service.UserInfoService;
import com.bilibili.common.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/user")
@Api(tags = "用户信息")
@Slf4j
@CrossOrigin(value = "*")
public class UserController {

    @Autowired
    UserInfoService userInfoService;

    @GetMapping("/getUserInfo/{selfid}/{visitedId}")
    @ApiOperation("获取用户信息")
    public Result<UserInfoVO> getUserInfo(
            @PathVariable Integer selfId,
            @PathVariable Integer visitedId) {
        return userInfoService.getUserInfo(selfId, visitedId);
    }

    @PostMapping("/editUserInfo")
    @ApiOperation("编辑用户信息")
    public Result<Boolean> editUserInfo(
            @RequestParam("file") MultipartFile file,
            @RequestParam("userId") Integer userId,
            @RequestParam("nickname") String nickname,
            @RequestParam("intro") String intro)
            throws Exception {
        log.info("编辑用户信息");
        return userInfoService.editSelfInfo(file, userId, nickname, intro);
    }

    @GetMapping("/test")
    public String test() {
        log.info("test is called");
        return "test";
    }
}
