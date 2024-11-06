package com.bilibili.video.controller;

import com.bilibili.common.util.Result;
import com.bilibili.video.domain.dto.CommentDTO;
import com.bilibili.video.domain.vo.VideoCommentVO;
import com.bilibili.video.service.CommentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
@RestController
@Api(tags = "评论和查看视频评论")
@RequestMapping("/comment")
@CrossOrigin(value = "*")
@Slf4j
public class CommentController {
    @Resource
    CommentService commentService;
    @PostMapping("/comment")
    @ApiOperation("发送对视频或评论的评论")
    public Result<Boolean> comment(@RequestBody CommentDTO comment) {
        log.info("评论评论");
        log.info(comment.toString());
        log.info("\n");
        log.info("\n");
        log.info("\n");
        return commentService.commentToComment(comment);
    }


    @GetMapping("/getComment/{videoId}/{userId}/{type}")
    @ApiOperation("获取视频下的评论")
    public Result<VideoCommentVO> getComment(@PathVariable Integer videoId, @PathVariable Integer userId,
                                             @PathVariable Integer type) {
        log.info("获取评论");
        log.info(userId.toString());
        log.info("\n");
        log.info("\n");
        log.info("\n");
        return commentService.getComment(videoId,userId,type);
    }
}
