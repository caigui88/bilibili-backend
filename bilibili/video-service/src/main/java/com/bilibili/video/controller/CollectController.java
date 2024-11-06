package com.bilibili.video.controller;

import com.bilibili.common.domain.entity.video.audience_reactions.CollectGroup;
import com.bilibili.common.util.Result;
import com.bilibili.video.domain.dto.CollectGroupDTO;
import com.bilibili.video.domain.dto.CollectDTO;
import com.bilibili.video.domain.vo.CollectGroupVO;
import com.bilibili.video.domain.vo.CollectVideoVO;
import com.bilibili.video.service.CollectService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController(value = "xollectController")
@Api(tags = "收藏和收藏夹的增删改查")
@RequestMapping("/collect")
@CrossOrigin(value = "*")
@Slf4j
public class CollectController {
    @Resource
    CollectService collectService;
    @PostMapping("/collect")
    @ApiOperation("收藏视频")
    public Result<Boolean> collect(@RequestBody List<CollectDTO> collectDTO) {
        log.info("收藏视频");
        log.info(collectDTO.toString());
        log.info("\n");
        log.info("\n");
        log.info("\n");
       return collectService.collect(collectDTO);

    }
    @GetMapping("/getVideoToCollectGroup/{userId}/{videoId}")
    @ApiOperation("获取视频页面收藏夹")
    public Result<List<CollectGroupVO>> getVideoToCollectGroup(@PathVariable Integer userId, @PathVariable Integer videoId){
        return collectService.getVideoToCollectGroup(userId,videoId);
    }

    @PostMapping("/editCollectGroup")
    @ApiOperation("创建、修改收藏夹")
    public Result<Boolean> editCollectGroup(@RequestBody CollectGroupDTO createCollectGroupDTO) {
        log.info("创建修改收藏夹");
        log.info(createCollectGroupDTO.toString());
        log.info("\n");
        log.info("\n");
        log.info("\n");
        return collectService.editCollectGroup(createCollectGroupDTO);
    }
    @PostMapping("/deleteCollectGroup")
    @ApiOperation("删除收藏夹")
    public Result<Boolean> deleteCollectGroup(@RequestBody CollectGroupDTO createCollectGroupDTO) {
        log.info("删除收藏夹");
        log.info(createCollectGroupDTO.toString());
        log.info("\n");
        log.info("\n");
        log.info("\n");
        return collectService.deleteCollectGroup(createCollectGroupDTO);
    }
    @GetMapping("/getCollectGroup/{userId}")
    @ApiOperation("获取首页收藏夹列表")
    public Result<List<CollectGroup>> getCollectGroup(@PathVariable Integer userId) {
        log.info("获取收藏夹列表");
        log.info(userId.toString());
        log.info("\n");
        log.info("\n");
        log.info("\n");
        return collectService.getCollectGroup(userId);
    }
    @GetMapping("/getCollectVideo/{collectGroupId}")
    @ApiOperation("获取首页某个收藏夹下的收藏夹视频")
    public Result<List<CollectVideoVO>> getCollectVideo(@PathVariable Integer collectGroupId) {
        return collectService.getCollectVideo(collectGroupId);
    }
}
