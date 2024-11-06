package com.bilibili.notice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.bilibili.common.domain.entity.chat.Chat;
import com.bilibili.common.domain.entity.notice.CommentNotice;
import com.bilibili.common.domain.entity.notice.DynamicToUser;
import com.bilibili.common.domain.entity.notice.LikeNotice;
import com.bilibili.common.domain.entity.video.audience_reactions.Comment;
import com.bilibili.common.domain.entity.video.video_production.Video;
import com.bilibili.common.mapper.chat.ChatMapper;
import com.bilibili.common.mapper.notice.DynamicToUserMapper;
import com.bilibili.common.mapper.video.audience_reactions.CommentMapper;
import com.bilibili.common.mapper.video.video_production.VideoMapper;
import com.bilibili.common.util.Result;
import com.bilibili.common.domain.entity.video.audience_reactions.Like;
import com.bilibili.notice.domain.vo.CommentNoticeVO;
import com.bilibili.notice.domain.vo.DynamicVideoVO;
import com.bilibili.notice.domain.vo.LikeNoticeVO;
import com.bilibili.notice.domain.vo.UnReadNoticeCountVO;
import com.bilibili.notice.mapper.NoticeServiceMapper;
import com.bilibili.notice.service.GetNoticeService;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class GetNoticeServiceImpl implements GetNoticeService {
    @Resource
    DynamicToUserMapper dynamicToUserMapper;
    @Resource
    ChatMapper chatMapper;
    @Resource
    CommentMapper commentMapper;
    @Resource
    VideoMapper videoMapper;
    @Resource
    NoticeServiceMapper noticeServiceMapper;
    @Override
    public Result<UnReadNoticeCountVO> getNoticeCount(Integer userId) {
        LambdaQueryWrapper<Chat> chatLambdaQueryWrapper = new LambdaQueryWrapper<>();
        MPJLambdaWrapper<Video> videoLikeLambdaQueryWrapper = new MPJLambdaWrapper<>();
        MPJLambdaWrapper<Comment> commentLikeLambdaQueryWrapper = new MPJLambdaWrapper<>();
        MPJLambdaWrapper<Video> videoLambdaQueryWrapper = new MPJLambdaWrapper<>();
        MPJLambdaWrapper<Comment> commentLamdaQueryWrapper=new MPJLambdaWrapper<>();
        LambdaQueryWrapper<DynamicToUser> dynamicToUserLambdaQueryWrapper = new LambdaQueryWrapper<>();
        chatLambdaQueryWrapper.eq(Chat::getReceiverId, userId);
        chatLambdaQueryWrapper.eq(Chat::getStatus,0);
        videoLikeLambdaQueryWrapper.eq(Video::getUserId, userId);
        videoLikeLambdaQueryWrapper.leftJoin(LikeNotice.class,LikeNotice::getVideoId,Video::getId);
        videoLikeLambdaQueryWrapper.eq(LikeNotice::getStatus,0);
        videoLikeLambdaQueryWrapper.isNull(LikeNotice::getCommentId);
        long videoLikeCount= videoMapper.selectJoinCount(videoLikeLambdaQueryWrapper);
        commentLikeLambdaQueryWrapper.eq(Comment::getUserId,userId);
        commentLikeLambdaQueryWrapper.eq(LikeNotice::getStatus,0);
        commentLikeLambdaQueryWrapper.leftJoin(LikeNotice.class,LikeNotice::getCommentId,Comment::getId);
        long commentLikeCount=commentMapper.selectJoinCount(commentLikeLambdaQueryWrapper);
        commentLikeLambdaQueryWrapper.eq(Like::getUserId, userId);
        dynamicToUserLambdaQueryWrapper.eq(DynamicToUser::getUserId, userId);
        dynamicToUserLambdaQueryWrapper.eq(DynamicToUser::getStatus,0);
        long chatCount = chatMapper.selectCount(chatLambdaQueryWrapper);
        long likeCount = videoLikeCount+commentLikeCount;
        long dynamicVideoCount = dynamicToUserMapper.selectCount(dynamicToUserLambdaQueryWrapper);
        videoLambdaQueryWrapper.eq(Video::getUserId,userId);
        videoLambdaQueryWrapper.leftJoin(CommentNotice.class,CommentNotice::getVideoId,Video::getId);
        videoLambdaQueryWrapper.isNull(CommentNotice::getReceiverId);
        videoLambdaQueryWrapper.eq(CommentNotice::getStatus,0);
        long videoCommentCount=videoMapper.selectJoinCount(videoLambdaQueryWrapper);
        commentLamdaQueryWrapper.eq(Comment::getUserId,userId);
        commentLamdaQueryWrapper.leftJoin(CommentNotice.class,CommentNotice::getReceiverId,Comment::getId);
        commentLamdaQueryWrapper.eq(CommentNotice::getStatus,0);
        long commentCommentCount=commentMapper.selectJoinCount(commentLamdaQueryWrapper);
        long commentCount=videoCommentCount+commentCommentCount;
        long totalCount = chatCount + likeCount + commentCount;
        return Result.data(new UnReadNoticeCountVO().setChatCount(chatCount).setLikeCount(likeCount)
                .setCommentCount(commentCount).setDynamicVideoCount(dynamicVideoCount).setTotalCount(totalCount));
    }
    @Override
    public Result<List<LikeNoticeVO>> getLikeNotice(Integer userId) {
        List<LikeNoticeVO> videoLikeNoticeVO =noticeServiceMapper.getVideoLikeNotice(userId);
        List<LikeNoticeVO> commentLikeNoticeVO =noticeServiceMapper.getCommentLikeNotice(userId);
        videoLikeNoticeVO.addAll(commentLikeNoticeVO);
        return Result.data(videoLikeNoticeVO);
    }
    @Override
    public Result<List<CommentNoticeVO>> getCommentNotice(Integer userId) {
        List<CommentNoticeVO> videoCommentNoticeVO =noticeServiceMapper.getCommentToVideoNotice(userId);
        List<CommentNoticeVO> commentToCommentNoticeVO =noticeServiceMapper.getCommentToCommentNotice(userId);
        videoCommentNoticeVO.addAll(commentToCommentNoticeVO);
        return Result.data(videoCommentNoticeVO);
    }
    @Override
    public Result<List<DynamicVideoVO>> getDynamicVideo(Integer userId) {
        return Result.data(noticeServiceMapper.getDynamicVideoNotice(userId));
    }


}
