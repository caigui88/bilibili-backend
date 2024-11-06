package com.bilibili.notice.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.bilibili.common.domain.entity.notice.CommentNotice;
import com.bilibili.common.domain.entity.notice.DynamicToUser;
import com.bilibili.common.domain.entity.notice.LikeNotice;
import com.bilibili.common.domain.entity.video.audience_reactions.Comment;
import com.bilibili.common.domain.entity.video.video_production.Video;
import com.bilibili.common.mapper.notice.DynamicToUserMapper;
import com.bilibili.common.mapper.video.audience_reactions.CommentMapper;
import com.bilibili.common.mapper.video.video_production.VideoMapper;
import com.bilibili.common.util.Result;
import com.bilibili.notice.domain.dto.CommentNoticeStatusChangeDTO;
import com.bilibili.notice.domain.dto.DynamicVideoStatusChangeDTO;
import com.bilibili.notice.domain.dto.LikeNoticeStatusChangeDTO;
import com.bilibili.notice.service.ChangeNoticeStatusService;
import com.github.yulichang.toolkit.JoinWrappers;
import com.github.yulichang.wrapper.UpdateJoinWrapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
//消息状态更新
@Service
public class ChangeNoticeStatusServiceImpl implements ChangeNoticeStatusService {
    @Resource
    DynamicToUserMapper dynamicToUserMapper;
    @Resource
    VideoMapper videoMapper;
    @Resource
    CommentMapper commentMapper;
    /**
     *修改点赞消息状态
     */
@Override
    public Result<Boolean> changeLikeNoticeStatus(LikeNoticeStatusChangeDTO likeNoticeStatusChangeDTO) {
    //修改对视频的点赞消息为已读
        UpdateJoinWrapper<Video> videoUpdateJoinWrapper= JoinWrappers.update(Video.class);
        videoUpdateJoinWrapper.eq(Video::getUserId, likeNoticeStatusChangeDTO.getUserId());
        videoUpdateJoinWrapper.leftJoin(LikeNotice.class,LikeNotice::getVideoId,Video::getId);
        videoUpdateJoinWrapper.isNull(LikeNotice::getCommentId);
        videoUpdateJoinWrapper.eq(LikeNotice::getStatus,0);
        videoUpdateJoinWrapper.set(LikeNotice::getStatus,1);
        videoMapper.updateJoin(null,videoUpdateJoinWrapper);
        //修改对评论的点赞消息为已读
        UpdateJoinWrapper<Comment> commentUpdateJoinWrapper=JoinWrappers.update(Comment.class);
        commentUpdateJoinWrapper.eq(Comment::getUserId, likeNoticeStatusChangeDTO.getUserId());
        commentUpdateJoinWrapper.leftJoin(LikeNotice.class,LikeNotice::getCommentId,Comment::getId);
        commentUpdateJoinWrapper.eq(LikeNotice::getStatus,0);
        commentUpdateJoinWrapper.set(LikeNotice::getStatus,1);
        commentMapper.updateJoin(null,commentUpdateJoinWrapper);
        return Result.success(true);
    }
    /**
     *修改评论消息状态
     */
    @Override
    public Result<Boolean> changeCommentNoticeStatus(CommentNoticeStatusChangeDTO commentNoticeStatusChangeDTO) {
        //修改对视频的评论的消息已读
        UpdateJoinWrapper<Video> videoUpdateJoinWrapper=JoinWrappers.update(Video.class);
        videoUpdateJoinWrapper.eq(Video::getUserId, commentNoticeStatusChangeDTO.getUserId());
        videoUpdateJoinWrapper.leftJoin(CommentNotice.class,CommentNotice::getVideoId,Video::getId);
        videoUpdateJoinWrapper.isNull(CommentNotice::getReceiverId);
        videoUpdateJoinWrapper.eq(CommentNotice::getStatus,0);
        videoUpdateJoinWrapper.set(CommentNotice::getStatus,1);
        videoMapper.updateJoin(null,videoUpdateJoinWrapper);
        //修改对评论的评论的消息已读
        UpdateJoinWrapper<Comment> commentUpdateJoinWrapper=JoinWrappers.update(Comment.class);
        commentUpdateJoinWrapper.eq(Comment::getUserId, commentNoticeStatusChangeDTO.getUserId());
        commentUpdateJoinWrapper.leftJoin(CommentNotice.class,CommentNotice::getReceiverId,Comment::getId);
        commentUpdateJoinWrapper.eq(CommentNotice::getStatus,0);
        commentUpdateJoinWrapper.set(CommentNotice::getStatus,1);
        commentMapper.updateJoin(null,commentUpdateJoinWrapper);
        return Result.success(true);
    }
    /**
     *修改推送动态状态
     */
    @Override
    public Result<Boolean> changeDynamicVideoStatus(DynamicVideoStatusChangeDTO videoStatusChangeRequest) {
        LambdaUpdateWrapper<DynamicToUser> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper.eq(DynamicToUser::getUserId, videoStatusChangeRequest.getUserId());
        updateWrapper.set(DynamicToUser::getStatus, 1);
        dynamicToUserMapper.update(null, updateWrapper);
        return Result.success(true);
    }
}
