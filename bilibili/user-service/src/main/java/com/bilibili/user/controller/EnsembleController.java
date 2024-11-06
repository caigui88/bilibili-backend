package com.bilibili.user.controller;

import com.bilibili.common.domain.entity.user.VideoEnsemble;
import com.bilibili.common.util.Result;
import com.bilibili.user.domain.dto.AddOrUpdateEnsembleDTO;
import com.bilibili.user.domain.dto.AddToEnsembleDTO;
import com.bilibili.user.domain.dto.DeleteFromEnsembleDTO;
import com.bilibili.user.domain.vo.AllVideoInEnsembleVO;
import com.bilibili.user.domain.vo.EnsembleVideoVO;
import com.bilibili.user.service.EnsembleService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@RequestMapping("/ensemble")
public class EnsembleController {
    @Resource
    EnsembleService ensembleService;

    @PostMapping("/getEnsembleList/{userId}")
    @ApiOperation("获取个人主页视频合集")
    public Result<List<VideoEnsemble>> getEnsembleList(
            @PathVariable Integer userId) {
        log.info("1");
        return ensembleService.getEnsembleList(userId);
    }

    @PostMapping("/addOrUpDaterEnsemble")
    @ApiOperation("创建或修改个人主页视频合集")
    public Result<Boolean> addOrUpDaterEnsemble(
            @RequestBody AddOrUpdateEnsembleDTO addOrUpdateEnsembleDTO) {
        log.info("1");
        return ensembleService.addOrUpdateEnsemble(addOrUpdateEnsembleDTO);
    }

    @GetMapping("/getEnsembleVideo/{userId}")

    @ApiOperation("获取个人主页视频合集中的视频")
    public Result<List<EnsembleVideoVO>> getEnsembleVideo(
            @PathVariable Integer userId) {
        log.info("1");
        return ensembleService.getEnsembleVideo(userId);
    }

    @GetMapping("/getAllVideoInEnsemble/{userId}/{videoId}")
    @ApiOperation("获取视频详情页和该视频在同一up主的视频合集下的其他视频")
    public Result<AllVideoInEnsembleVO> getAllVideoInEnsemble(
            @PathVariable Integer userId,
            @PathVariable Integer videoId) {
        return ensembleService.getAllVideoInEnsemble(userId, videoId);
    }

    @ApiOperation("把视频添加进视频合集")
    @PostMapping("/addToEnsemble")
    public Result<Boolean> addToEnsemble(
            @RequestBody AddToEnsembleDTO addToEnsembleDTO) {
        log.info("1");
        return ensembleService.addToEnsemble(addToEnsembleDTO);
    }

    @ApiOperation("把视频从合集中移除")
    @PostMapping("/deleteFromEnsemble")
    public Result<Boolean> deleteFromEnsemble(
            @RequestBody DeleteFromEnsembleDTO deleteFromEnsembleDTO) {
        log.info("1");
        return ensembleService.deleteFromEnsemble(deleteFromEnsembleDTO);
    }
}
