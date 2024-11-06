package com.bilibili.video.controller;

import com.bilibili.common.util.Result;
import com.bilibili.video.domain.dto.DeleteHistoryVideoDTO;
import com.bilibili.video.domain.vo.CommendVideoVO;
import com.bilibili.video.domain.vo.DetailVideoVO;
import com.bilibili.video.domain.vo.FirstPageVideoVO;
import com.bilibili.video.domain.vo.HistoryVideoVO;
import com.bilibili.video.service.PlayService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@Api(tags = "历史记录和视频播放")
@RequestMapping("/play")
@Slf4j
public class PlayController {
    @Resource
    PlayService playService;
    @ApiOperation("删除历史播放视频")
    @PostMapping("/deleteHistoryVideo")
    public Result<Boolean> deleteHistoryVideo(@RequestBody DeleteHistoryVideoDTO deleteHistoryVideoDTO) {
        log.info("1");
        return playService.deleteHistoryVideo(deleteHistoryVideoDTO);
    }

    @ApiOperation("增加历史播放视频")
    @PostMapping("/addPlayRecord/{videoId}/{userId}")
    public Result<Boolean> addPlayRecord(@PathVariable Integer videoId, @PathVariable Integer userId) {
        log.info("增加历史播放视频");
        log.info(videoId.toString());
        log.info(userId.toString());
        log.info("\n");
        log.info("\n");
        log.info("\n");
        return playService.addPlayRecord(videoId, userId);
    }

    @GetMapping("/getHistoryVideo/{userId}")
    @ApiOperation("获取首页历史视频")
    public Result<List<HistoryVideoVO>> getHistoryVideo(@PathVariable Integer userId) {
        log.info("1");
        return playService.getHistoryVideo(userId);
    }

    @GetMapping("/getDetailVideo/{videoId}/{userId}/{collectGroupId}")
    @ApiOperation("视频详情页的视频信息")
    public Result<DetailVideoVO> getDetailVideo(@PathVariable Integer videoId, @PathVariable Integer userId, @PathVariable String collectGroupId) {
        log.info("1");
        return playService.getDetailVideo(videoId, userId, collectGroupId);
    }

    @GetMapping("/getCommendVideo/{videoId}")
    @ApiOperation("获取推荐视频")
    public Result<List<CommendVideoVO>> getRecommendVideo(@PathVariable String videoId) {
        log.info("1");
        return playService.getRecommendVideo(videoId);
    }

    @GetMapping("/getFirstPageVideo/{count}")
    @ApiOperation("首页视频列表")
    public Result<List<FirstPageVideoVO>> getFirstPageVideoResponse(@PathVariable Integer count) {
        return playService.getFirstPageVideoResponse(count);
    }
}
