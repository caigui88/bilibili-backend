package com.bilibili.notice.controller;

import com.bilibili.common.util.Result;
import com.bilibili.notice.domain.dto.CommentNoticeStatusChangeDTO;
import com.bilibili.notice.domain.dto.DynamicVideoStatusChangeDTO;
import com.bilibili.notice.domain.dto.LikeNoticeStatusChangeDTO;
import com.bilibili.notice.service.ChangeNoticeStatusService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Slf4j
@Api(tags = "修改未读评论、点赞状态")
@RestController
@RequestMapping("/changeNoticeStatus")
public class ChangeNoticeStatusController {
    @Resource
    ChangeNoticeStatusService changeNoticeStatusService;
    @PostMapping("/changeLikeNoticeStatus")
    @ApiOperation("修改点赞未读状态为已读")
    public Result<Boolean> changeLikeNoticeStatus(@RequestBody LikeNoticeStatusChangeDTO likeNoticeStatusChangeDTO){
        return changeNoticeStatusService.changeLikeNoticeStatus(likeNoticeStatusChangeDTO);
    }
    @PostMapping("/changeCommentNoticeStatus")
    @ApiOperation("修改评论未读状态为已读")
    public Result<Boolean> changeCommentNoticeStatus(@RequestBody CommentNoticeStatusChangeDTO commentNoticeStatusChangeDTO){
        return changeNoticeStatusService.changeCommentNoticeStatus(commentNoticeStatusChangeDTO);
    }

    @ApiOperation("修改动态视频状态为已读")
    @PostMapping("/changeDynamicVideoStatus")
    public Result<Boolean> changeDynamicVideoStatus(@RequestBody DynamicVideoStatusChangeDTO dynamicVideoStatusChangeDTO) {
        log.info("增加已读动态视频");
        log.info(dynamicVideoStatusChangeDTO.toString());
        log.info("\n");
        log.info("\n");
        log.info("\n");
        return changeNoticeStatusService.changeDynamicVideoStatus(dynamicVideoStatusChangeDTO);
    }
}
