package com.bilibili.video.service.impl;

import com.bilibili.common.domain.entity.notice.CommentNotice;
import com.bilibili.common.domain.entity.notice.DynamicToUser;
import com.bilibili.common.domain.entity.notice.LikeNotice;
import com.bilibili.common.domain.entity.user.Follow;
import com.bilibili.common.domain.entity.user.Privilege;
import com.bilibili.common.domain.entity.user.User;
import com.bilibili.common.domain.entity.user.VideoEnsemble;
import com.bilibili.common.domain.entity.video.audience_reactions.*;
import com.bilibili.common.domain.entity.video.video_production.Video;
import com.bilibili.common.domain.entity.video.video_production.VideoData;
import com.bilibili.common.mapper.notice.CommentNoticeMapper;
import com.bilibili.common.mapper.notice.DynamicToUserMapper;
import com.bilibili.common.mapper.notice.LikeNoticeMapper;
import com.bilibili.common.mapper.user.FollowMapper;
import com.bilibili.common.mapper.user.PrivilegeMapper;
import com.bilibili.common.mapper.user.UserMapper;
import com.bilibili.common.mapper.user.VideoEnsembleMapper;
import com.bilibili.common.mapper.video.audience_reactions.*;
import com.bilibili.common.mapper.video.video_production.VideoDataMapper;
import com.bilibili.common.mapper.video.video_production.VideoMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
//@AllArgsConstructor

public class GetAllTableDataService {
    @Resource
    LikeNoticeMapper likeNoticeMapper;
    @Resource
    CommentNoticeMapper commentNoticeMapper;
    @Resource
    CollectMapper collectMapper;
    @Resource
    CollectGroupMapper collectGroupMapper;
    @Resource
    PlayMapper playMapper;
    @Resource
    CommentMapper commentMapper;
    @Resource
    DanmakuMapper danmakuMapper;
    @Resource
    LikeMapper likeMapper;
    @Resource
    FollowMapper followMapper;
    @Resource
    UserMapper userMapper;
    @Resource
    VideoDataMapper videoDataMapper;
    @Resource
    AddToEnsembleMapper addToEnsembleMapper;
    @Resource
    VideoEnsembleMapper videoEnsembleMapper;
    @Resource
    VideoMapper videoMapper;
    @Resource
    PrivilegeMapper privilegeMapper;
    @Resource
    DynamicToUserMapper dynamicToUserMapper;

    public List<Privilege> getPrivilege(){
        return privilegeMapper.selectList(null);
    }
    public List<LikeNotice> getLikeNotice(){
        return likeNoticeMapper.selectList(null);
    }
    public List<CommentNotice> getCommentNotice(){
        return commentNoticeMapper.selectList(null);
    }
    public String getVideo(){
        List<Video> list=videoMapper.selectList(null);
        String response="";
        for(Video video : list){
            response+=video.getName()+",";
        }
        return response;
    }

    public List<VideoEnsemble> getVideoEnsemble(){
        return videoEnsembleMapper.selectList(null);
    }

    public List<AddToEnsemble> getAddToEnsemble(){
        return addToEnsembleMapper.selectList(null);
    }

    public List<VideoData> getVideoData(){
        return videoDataMapper.selectList(null);
    }

    public List<Collect> getCollect() {
    return collectMapper.selectList(null);
    }

    public List<CollectGroup> getCollectGroup() {
        return collectGroupMapper.selectList(null);
    }

    public List<Comment> getComment() {
        return commentMapper.selectList(null);
    }

    public List<Danmaku> getDanmaku() {
        return danmakuMapper.selectList(null);
    }

    public List<Like> getCommentLike() {
        return likeMapper.selectList(null);
    }

    public List<Follow> getFollow() {
        return followMapper.selectList(null);
    }

    public List<User> getUser() {
        return userMapper.selectList(null);
    }

    public List<Play> getHistoryPlay() {
        return playMapper.selectList(null);
    }

    public List<DynamicToUser> getDynamicToUser(){
        return dynamicToUserMapper.selectList(null);
    }
}
