package com.bilibili.notice.controller;

import com.bilibili.common.util.Result;
import com.bilibili.notice.domain.vo.CommentNoticeVO;
import com.bilibili.notice.domain.vo.DynamicVideoVO;
import com.bilibili.notice.domain.vo.LikeNoticeVO;
import com.bilibili.notice.domain.vo.UnReadNoticeCountVO;
import com.bilibili.notice.service.GetNoticeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/getNotice")
@Api(tags = "未读点赞数、评论数、回复数的获取，历史点赞、评论消息的获取")
@Slf4j
public class GetNoticeController {
    @Resource
    GetNoticeService getNoticeService;
    @ApiOperation("获取未读评论数、点赞数和回复数（以及三个加起来的未读消息数），获取未读的动态数量")
    @GetMapping("/getNoticeCount/{userId}")
    public Result<UnReadNoticeCountVO> getNoticeCount(@PathVariable Integer userId) {
        log.info("1");
        return getNoticeService.getNoticeCount(userId);
    }
    @GetMapping("/getLikeNotice/{userId}")
    @ApiOperation("获取历史点赞消息，包括已读的和未读的")
    public Result<List<LikeNoticeVO>> getLikeNotice(@PathVariable Integer userId){
        log.info("1");
       return getNoticeService.getLikeNotice(userId);
    }
    @ApiOperation("获取历史评论消息，包括已读的和未读的")
    @GetMapping("/getCommentNotice/{userId}")
    public Result<List<CommentNoticeVO>> getCommentNotice(@PathVariable Integer userId){
        log.info("1");
        return getNoticeService.getCommentNotice(userId);
    }
    @GetMapping("/getDynamicVideo/{userId}")
    @ApiOperation("获取首页动态视频")
    public Result<List<DynamicVideoVO>> getDynamicVideo(@PathVariable Integer userId) {
        log.info("1");
        return getNoticeService.getDynamicVideo(userId);
    }

}
