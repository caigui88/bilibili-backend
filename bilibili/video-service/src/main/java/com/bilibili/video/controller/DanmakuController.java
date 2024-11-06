package com.bilibili.video.controller;

import com.bilibili.common.util.Result;
import com.bilibili.video.domain.dto.AddDanmakuDTO;
import com.bilibili.video.domain.vo.DanmakuVO;
import com.bilibili.video.service.DanmakuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@Api(tags = "发送和获取视频弹幕")
@RequestMapping("/danmaku")
@CrossOrigin(value = "*")
@Slf4j
public class DanmakuController {
    @Resource
    DanmakuService danmakuService;
    @GetMapping("/getDanmaku/{videoId}")
    @ApiOperation("获取视频弹幕")
    public Result<List<DanmakuVO>> getDanmaku(@PathVariable Integer videoId) {
        log.info("获取视频弹幕");
        log.info(videoId.toString());
        log.info("\n");
        log.info("\n");
        log.info("\n");
        return danmakuService.getDanmaku(videoId);
    }
    @ApiOperation("发送弹幕")
    @PostMapping("/addDanmaku")
    public Result<Boolean> addDanmaku(@RequestBody AddDanmakuDTO addDanmakuDTO) {
        log.info("发送视频弹幕");
        log.info(addDanmakuDTO.toString());
        log.info("\n");
        log.info("\n");
        log.info("\n");
      return danmakuService.addDanmaku(addDanmakuDTO);

    }
}
