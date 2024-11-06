package com.bilibili.video.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.bilibili.common.domain.entity.video.audience_reactions.Danmaku;
import com.bilibili.common.mapper.video.audience_reactions.DanmakuMapper;
import com.bilibili.common.util.Result;
import com.bilibili.video.domain.dto.AddDanmakuDTO;
import com.bilibili.video.domain.vo.DanmakuVO;
import com.bilibili.video.service.DanmakuService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
/**
 *弹幕
 */
@Service
public class DanmakuServiceImpl implements DanmakuService {
    @Resource
    DanmakuMapper danmakuMapper;
    /**
     *新增弹幕
     */

    @Override
    public Result<Boolean> addDanmaku(AddDanmakuDTO addDanmakuDTO) {
        danmakuMapper.insert(addDanmakuDTO.toEntity());
        return Result.success(true);
    }
    /**
     *获取弹幕
     */
    @Override
    public Result<List<DanmakuVO>> getDanmaku(Integer videoId) {
        LambdaQueryWrapper<Danmaku> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(Danmaku::getVideoId,videoId);
        List<Danmaku> danmakus=danmakuMapper.selectList(queryWrapper);
        List<DanmakuVO> danmakuVOList = new ArrayList<>();
        for(Danmaku danmaku : danmakus){
            danmakuVOList.add(new DanmakuVO(danmaku));
        }
        return Result.data(danmakuVOList);
    }

}
