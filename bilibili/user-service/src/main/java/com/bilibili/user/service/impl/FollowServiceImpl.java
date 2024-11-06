package com.bilibili.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.bilibili.user.service.FollowService;
import com.bilibili.common.domain.entity.user.User;
import com.bilibili.common.domain.entity.user.Follow;
import com.bilibili.common.domain.entity.user.IdCount;
import com.bilibili.common.mapper.user.FollowMapper;
import com.bilibili.user.domain.dto.FollowDTO;
import com.bilibili.user.domain.vo.IdolOrFansVO;
import com.bilibili.common.util.Result;
import com.github.yulichang.wrapper.MPJLambdaWrapper;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//关注
@Service
public class FollowServiceImpl implements FollowService {
    @Resource
    FollowMapper followMapper;

    /**
     * 关注其他用户
     * @param follow
     * @return
     */
    @Override
    public Result<Boolean> follow(FollowDTO follow) {
        followMapper.insert(follow.toEntity());
        return Result.success(true);
    }

    /**
     * 取关其他用户
     * @param follow
     * @return
     */
    @Override
    public Result<Boolean> recallFollow(FollowDTO follow) {
        LambdaQueryWrapper<Follow> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Follow::getIdolId, follow.getIdolId());
        wrapper.eq(Follow::getFansId, follow.getFansId());
        followMapper.delete(wrapper);
        return Result.success(true);
    }

    /**
     * 获取关注列表或粉丝列表
     * @param userId
     * @param type
     * @return
     */
    @Override
    public Result<List<IdolOrFansVO>> getIdolOrFansListVO(int userId, int type) {
        MPJLambdaWrapper<Follow> wrapper = new MPJLambdaWrapper<>();
        //根据类型判断是要获取关注还是粉丝列表
        if (type == 0) {
            wrapper.eq(Follow::getFansId, userId);
            wrapper.innerJoin(User.class, User::getId, Follow::getIdolId);
        } else {
            wrapper.eq(Follow::getIdolId, userId);
            wrapper.innerJoin(User.class, User::getId, Follow::getFansId);
        }
        wrapper.selectAs(User::getNickname, IdolOrFansVO::getNickname);
        wrapper.selectAs(User::getCover, IdolOrFansVO::getCover);
        wrapper.selectAs(User::getId, IdolOrFansVO::getUserId);
        wrapper.select(Follow::getCreateTime);
        wrapper.orderByDesc(Follow::getCreateTime);
        List<IdolOrFansVO> VOs = followMapper.selectJoinList(IdolOrFansVO.class, wrapper);
        List<Integer> ids = new ArrayList<>();
        for (IdolOrFansVO VO : VOs) {
            ids.add(VO.getUserId());
        }
        //获取关注列表和粉丝列表的关注数和粉丝数并封装到响应集合里
        List<IdCount> fansCountList = followMapper.getFansCount(ids);
        List<IdCount> idolCountList = followMapper.getIdolCount(ids);
        Map<Integer, Integer> fansCountMap = new HashMap<>(10);
        Map<Integer, Integer> idolCountMap = new HashMap<>(10);
        for (IdCount VO : fansCountList) {
            fansCountMap.put(VO.getId(), VO.getCount());
        }
        for (IdCount VO : idolCountList) {
            idolCountMap.put(VO.getId(), VO.getCount());
        }
        for (IdolOrFansVO VO : VOs) {
            VO.setIdolCount(idolCountMap.get(VO.getUserId()))
                    .setFansCount(fansCountMap.get(VO.getUserId()));
        }
        return Result.data(VOs);
    }

}
