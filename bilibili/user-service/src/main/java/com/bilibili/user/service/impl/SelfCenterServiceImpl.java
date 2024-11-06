package com.bilibili.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.bilibili.user.domain.vo.*;
import com.bilibili.user.mapper.UserCenterServiceMapper;
import com.bilibili.user.service.FollowService;
import com.bilibili.common.domain.entity.user.Privilege;
import com.bilibili.common.domain.entity.user.User;
import com.bilibili.common.domain.entity.user.Follow;
import com.bilibili.common.domain.entity.user.IdCount;
import com.bilibili.common.domain.entity.video.audience_reactions.Like;
import com.bilibili.common.domain.entity.video.video_production.Video;
import com.bilibili.common.domain.entity.video.video_production.VideoData;
import com.bilibili.common.mapper.user.PrivilegeMapper;
import com.bilibili.common.mapper.user.FollowMapper;
import com.bilibili.common.mapper.video.audience_reactions.LikeMapper;
import com.bilibili.common.mapper.video.video_production.VideoMapper;
import com.bilibili.user.domain.dto.EditUserCenterPrivilegeDTO;
import com.bilibili.user.service.SelfCenterService;
import com.bilibili.common.util.Result;
import com.github.yulichang.wrapper.MPJLambdaWrapper;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 *个人主页
 */
@Service
public class SelfCenterServiceImpl implements SelfCenterService {
    String sql="limit 10";
    @Resource
    VideoMapper videoMapper;
    @Resource
    PrivilegeMapper privilegeMapper;

    @Resource
    LikeMapper likeMapper;
    @Resource
    UserCenterServiceMapper userCenterServiceMapper;
    @Resource
    FollowService followService;
    @Resource
    FollowMapper followMapper;
    /**
     *获取某用户个人主页权限，并根据个人主页权限决定是否要返回对应模块内容
     */
    @Override
    public Result<SelfCenterContentVO> getPersonalCenterContent(Integer selfId, Integer visitedId) {
        LambdaQueryWrapper<Privilege> privilegeWrapper=new LambdaQueryWrapper<>();
        privilegeWrapper.eq(Privilege::getUserId,visitedId);
        Privilege privilege=privilegeMapper.selectOne(privilegeWrapper);
        if(selfId.equals(visitedId)){
            privilege.setCollectGroup(0).setRecentlyLike(0).setFansList(0).setIdolList(0);
        }
        //投稿视频都公开，因此不需要查询权限后再确认是否需要返回
        SelfCenterContentVO selfCenterContentVO=new SelfCenterContentVO();
        MPJLambdaWrapper<Video> wrapper=new MPJLambdaWrapper<>();
        wrapper.eq(Video::getUserId,visitedId);
        wrapper.selectAs(Video::getId, SelfVideoVO::getVideoId);
        wrapper.orderByDesc(Video::getCreateTime);
        wrapper.select(Video::getCreateTime,Video::getCover,Video::getLength,Video::getName);
        wrapper.select(VideoData::getPlayCount);
        wrapper.leftJoin(VideoData.class,VideoData::getVideoId,Video::getId);
        List<SelfVideoVO> selfVideoVOList=videoMapper.selectJoinList(SelfVideoVO.class,wrapper);
        selfCenterContentVO.setSelfVideoVOList(selfVideoVOList);
        //收藏夹
        if(privilege.getCollectGroup()==0){
            List<SelfCollectVO> selfCollectVOs= userCenterServiceMapper.getSelfCollect(visitedId);
            selfCenterContentVO.setSelfCollectVOList(selfCollectVOs);
        }
        //粉丝列表
        if(privilege.getFansList()==0){
            MPJLambdaWrapper<Follow> fansWrapper=new MPJLambdaWrapper<>();
            fansWrapper.eq(Follow::getIdolId,visitedId);
            fansWrapper.innerJoin(User.class, User::getId,Follow::getFansId);
            fansWrapper.selectAs(User::getNickname, IdolOrFansVO::getNickname);
            fansWrapper.selectAs(User::getCover,IdolOrFansVO::getCover);
            fansWrapper.selectAs(User::getId, IdolOrFansVO::getUserId);
            fansWrapper.select(Follow::getCreateTime);
            fansWrapper.orderByDesc(Follow::getCreateTime);
            List<IdolOrFansVO> fansVOs= followMapper.selectJoinList(IdolOrFansVO.class,fansWrapper);
            List<Integer> ids=new ArrayList<>();
            for(IdolOrFansVO VO : fansVOs){
                ids.add(VO.getUserId());
            }
            if(ids.size()>0){
                List<IdCount> fansCountList= followMapper.getFansCount(ids);
                List<IdCount> idolCountList= followMapper.getIdolCount(ids);
                Map<Integer,Integer> fansCountMap=new HashMap<>(10);
                Map<Integer,Integer> idolCountMap=new HashMap<>(10);
                for(IdCount VO : fansCountList){
                    fansCountMap.put(VO.getId(),VO.getCount());
                }
                for(IdCount VO : idolCountList){
                    idolCountMap.put(VO.getId(),VO.getCount());
                }
                for(IdolOrFansVO VO : fansVOs){
                    if(idolCountMap.get(VO.getUserId())!=null){
                        VO.setIdolCount(idolCountMap.get(VO.getUserId()));
                    }
                    if(fansCountMap.get(VO.getUserId())!=null){
                        VO.setFansCount(fansCountMap.get(VO.getUserId()));
                    }
                }
                selfCenterContentVO.setFansVOList(fansVOs);
            }
        }
        //关注列表
        if(privilege.getIdolList()==0){
            MPJLambdaWrapper<Follow> idolWrapper=new MPJLambdaWrapper<>();
            idolWrapper.eq(Follow::getFansId,visitedId);
            idolWrapper.innerJoin(User.class, User::getId,Follow::getIdolId);
            idolWrapper.selectAs(User::getNickname, IdolOrFansVO::getNickname);
            idolWrapper.selectAs(User::getCover,IdolOrFansVO::getCover);
            idolWrapper.selectAs(User::getId, IdolOrFansVO::getUserId);
            idolWrapper.select(Follow::getCreateTime);
            idolWrapper.orderByDesc(Follow::getCreateTime);
            List<IdolOrFansVO> idolListVOs= followMapper.selectJoinList(IdolOrFansVO.class,idolWrapper);
            List<Integer> ids=new ArrayList<>();
            for(IdolOrFansVO VO : idolListVOs){
                ids.add(VO.getUserId());
            }
            if(ids.size()>0){
                List<IdCount> fansCountList= followMapper.getFansCount(ids);
                List<IdCount> idolCountList= followMapper.getIdolCount(ids);
                Map<Integer,Integer> fansCountMap=new HashMap<>(10);
                Map<Integer,Integer> idolCountMap=new HashMap<>(10);
                for(IdCount VO : fansCountList){
                    fansCountMap.put(VO.getId(),VO.getCount());
                }
                for(IdCount VO : idolCountList){
                    idolCountMap.put(VO.getId(),VO.getCount());
                }
                for(IdolOrFansVO VO : idolListVOs){
                    VO.setIdolCount(idolCountMap.get(VO.getUserId()))
                            .setFansCount(fansCountMap.get(VO.getUserId()));
                }
                selfCenterContentVO.setIdolVOList(idolListVOs);
            }
        }
        //最近点赞
        if(privilege.getRecentlyLike()==0){
            MPJLambdaWrapper<Like> remotelyLikeWrapper=new MPJLambdaWrapper<>();
            remotelyLikeWrapper.eq(Like::getUserId,visitedId);
            remotelyLikeWrapper.isNull(Like::getCommentId);
            remotelyLikeWrapper.orderByDesc(Like::getCreateTime);
            remotelyLikeWrapper.selectAs(Video::getId, RecentlyLikeVideoVO::getVideoId);
            remotelyLikeWrapper.select(Video::getCreateTime,Video::getCover,Video::getLength,Video::getName);
            remotelyLikeWrapper.select(VideoData::getPlayCount,VideoData::getDanmakuCount);
            remotelyLikeWrapper.leftJoin(Video.class,Video::getId, Like::getVideoId);
            remotelyLikeWrapper.last(sql);
            remotelyLikeWrapper.leftJoin(VideoData.class,VideoData::getVideoId,Video::getId);
            List<RecentlyLikeVideoVO> remotelyLikeVideoVOs=likeMapper.selectJoinList(RecentlyLikeVideoVO.class,remotelyLikeWrapper);
            selfCenterContentVO.setRecentlyLikeVideoVOList(remotelyLikeVideoVOs);
        }
        return Result.data(selfCenterContentVO);
    }
    /**
     *获取个人主页权限
     */
    @Override
    public Result<PrivilegeVO> getUserPrivilege(Integer userId){
        LambdaQueryWrapper<Privilege> wrapper=new LambdaQueryWrapper<>();
        wrapper.eq(Privilege::getUserId,userId);
        Privilege privilege=privilegeMapper.selectOne(wrapper);
        return Result.data(new PrivilegeVO().setCollectGroup(privilege.getCollectGroup()).setFansList(privilege.getFansList()).setIdolList(privilege.getIdolList()).setRemotelyLike(privilege.getRecentlyLike()));
    }
    /**
     *编辑个人主页权限
     */
    @Override
    public Result<Boolean> editUserPrivilege(EditUserCenterPrivilegeDTO editUserCenterPrivilegeDTO){
        LambdaUpdateWrapper<Privilege> wrapper=new LambdaUpdateWrapper<>();
        wrapper.eq(Privilege::getUserId, editUserCenterPrivilegeDTO.getUserId());
        privilegeMapper.update(editUserCenterPrivilegeDTO.toEntity(),wrapper);
        return Result.success(true);
    }


}
