package com.bilibili.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.bilibili.user.domain.dto.AddOrUpdateEnsembleDTO;
import com.bilibili.user.domain.dto.AddToEnsembleDTO;
import com.bilibili.user.domain.dto.DeleteFromEnsembleDTO;
import com.bilibili.user.domain.vo.AllVideoInEnsembleVO;
import com.bilibili.user.domain.vo.VideoInEnsembleVO;
import com.bilibili.user.service.EnsembleService;
import com.bilibili.common.domain.entity.user.VideoEnsemble;
import com.bilibili.common.domain.entity.video.audience_reactions.AddToEnsemble;
import com.bilibili.common.domain.entity.video.video_production.Video;
import com.bilibili.common.domain.entity.video.video_production.VideoData;
import com.bilibili.common.mapper.user.VideoEnsembleMapper;
import com.bilibili.common.mapper.video.audience_reactions.AddToEnsembleMapper;
import com.bilibili.user.domain.vo.EnsembleVideoVO;
import com.bilibili.common.util.Result;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

//该类后续会补上对应前端
@Service
public class EnsembleServiceImpl implements EnsembleService {
    @Resource
    VideoEnsembleMapper videoEnsembleMapper;
    @Resource
    AddToEnsembleMapper addToEnsembleMapper;

    /**
     * 获取用户主页的视频合集
     *
     * @param userId
     * @return
     */
    @Override
    public Result<List<VideoEnsemble>> getEnsembleList(Integer userId) {
        LambdaQueryWrapper<VideoEnsemble> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(VideoEnsemble::getUserId, userId);
        wrapper.orderByDesc(VideoEnsemble::getCreateTime);
        return Result.data(videoEnsembleMapper.selectList(wrapper));
    }

    /**
     * 新增或修改合集
     *
     * @param addOrUpdateEnsembleDTO
     * @return
     */
    @Override
    public Result<Boolean> addOrUpdateEnsemble(AddOrUpdateEnsembleDTO addOrUpdateEnsembleDTO) {
        if (addOrUpdateEnsembleDTO.getId() != null) {
            videoEnsembleMapper.updateById(addOrUpdateEnsembleDTO.toEntity());

        } else {
            videoEnsembleMapper.insert(addOrUpdateEnsembleDTO.toEntity());
        }
        return Result.success(true);
    }

    /**
     * 获取个人主页视频合集中的视频
     *
     * @param userId
     * @return
     */
    @Override
    public Result<List<EnsembleVideoVO>> getEnsembleVideo(int userId) {
        MPJLambdaWrapper<VideoEnsemble> wrapper = new MPJLambdaWrapper<>();
        wrapper.eq(VideoEnsemble::getUserId, userId);
        wrapper.orderByDesc(AddToEnsemble::getCreateTime);
        wrapper.innerJoin(AddToEnsemble.class, AddToEnsemble::getEnsembleId, VideoEnsemble::getId);
        wrapper.innerJoin(Video.class, Video::getId, AddToEnsemble::getVideoId);
        wrapper.innerJoin(VideoData.class, VideoData::getVideoId, Video::getId);
        wrapper.selectAs(Video::getId, EnsembleVideoVO::getVideoId);
        wrapper.select(Video::getCreateTime, Video::getCover, Video::getLength, Video::getName);
        wrapper.select(VideoData::getPlayCount);
        wrapper.selectAs(VideoEnsemble::getId, EnsembleVideoVO::getEnsembleId);
        return Result.data(videoEnsembleMapper.selectJoinList(EnsembleVideoVO.class, wrapper));
    }

    /**
     * 获取视频详情页和与该视频在同一个合集下的其他视频
     *
     * @param userId
     * @param videoId
     * @return
     */
    @Override
    public Result<AllVideoInEnsembleVO> getAllVideoInEnsemble(Integer userId, Integer videoId) {
        AllVideoInEnsembleVO allVideoInEnsembleVO = new AllVideoInEnsembleVO();
        MPJLambdaWrapper<VideoEnsemble> getEnsembleIdMPJLambdaWrapper = new MPJLambdaWrapper<>();
        getEnsembleIdMPJLambdaWrapper.eq(VideoEnsemble::getUserId, userId);
        getEnsembleIdMPJLambdaWrapper.innerJoin(AddToEnsemble.class, AddToEnsemble::getEnsembleId, VideoEnsemble::getId);
        getEnsembleIdMPJLambdaWrapper.eq(AddToEnsemble::getVideoId, videoId);
        Map<String, Object> map = videoEnsembleMapper.selectJoinMap(getEnsembleIdMPJLambdaWrapper);
        if (map != null) {
            Integer ensembleId = (Integer) map.get("id");
            MPJLambdaWrapper<VideoEnsemble> videoEnsembleMPJLambdaWrapper = new MPJLambdaWrapper<>();
            videoEnsembleMPJLambdaWrapper.eq(VideoEnsemble::getId, ensembleId);
            videoEnsembleMPJLambdaWrapper.orderByDesc(AddToEnsemble::getCreateTime);
            videoEnsembleMPJLambdaWrapper.innerJoin(AddToEnsemble.class, AddToEnsemble::getEnsembleId, VideoEnsemble::getId);
            videoEnsembleMPJLambdaWrapper.innerJoin(Video.class, Video::getId, AddToEnsemble::getVideoId);
            videoEnsembleMPJLambdaWrapper.selectAs(Video::getId, VideoInEnsembleVO::getVideoId);
            videoEnsembleMPJLambdaWrapper.selectAs(Video::getName, VideoInEnsembleVO::getVideoName);
            videoEnsembleMPJLambdaWrapper.select(Video::getLength);
            videoEnsembleMPJLambdaWrapper.selectAs(VideoEnsemble::getName, VideoInEnsembleVO::getEnsembleName);
            videoEnsembleMPJLambdaWrapper.selectAs(VideoEnsemble::getId, VideoInEnsembleVO::getEnsembleId);
            List<VideoInEnsembleVO> list = videoEnsembleMapper.selectJoinList(VideoInEnsembleVO.class, videoEnsembleMPJLambdaWrapper);
            MPJLambdaWrapper<VideoEnsemble> totalVideoCountWrapper = new MPJLambdaWrapper<>();
            totalVideoCountWrapper.eq(VideoEnsemble::getId, ensembleId);
            totalVideoCountWrapper.innerJoin(AddToEnsemble.class, AddToEnsemble::getEnsembleId, VideoEnsemble::getId);
            totalVideoCountWrapper.innerJoin(Video.class, Video::getId, AddToEnsemble::getVideoId);
            allVideoInEnsembleVO.setTotalVideoCount(videoEnsembleMapper.selectJoinCount(totalVideoCountWrapper));
            List<Integer> ids = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                int id = list.get(i).getVideoId();
                if (id == videoId) {
                    allVideoInEnsembleVO.setPlaceInTotal(i + 1);
                }
                ids.add(id);
            }
            allVideoInEnsembleVO.setTotalPlayCount(videoEnsembleMapper.getTotalPlayCount(ids));
            allVideoInEnsembleVO.setVideoInEnsembleVOList(list);
            return Result.data(allVideoInEnsembleVO);
        }
        return Result.data(null);
    }

    /**
     * 把视频添加到合集
     *
     * @param addToEnsembleDTO
     * @return
     */
    @Override
    public Result<Boolean> addToEnsemble(AddToEnsembleDTO addToEnsembleDTO) {
        LambdaQueryWrapper<AddToEnsemble> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AddToEnsemble::getEnsembleId, addToEnsembleDTO.getEnsembleId());
        wrapper.eq(AddToEnsemble::getVideoId, addToEnsembleDTO.getVideoId());
        if (addToEnsembleMapper.selectOne(wrapper) != null) {
            return Result.data(false);
        }
        addToEnsembleMapper.insert(addToEnsembleDTO.toEntity());
        return Result.data(true);
    }

    /**
     * 将视频从合集中删除
     *
     * @param deleteFromEnsembleDTO
     * @return
     */
    @Override
    public Result<Boolean> deleteFromEnsemble(DeleteFromEnsembleDTO deleteFromEnsembleDTO) {
        LambdaQueryWrapper<AddToEnsemble> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AddToEnsemble::getEnsembleId, deleteFromEnsembleDTO.getEnsembleId());
        wrapper.eq(AddToEnsemble::getVideoId, deleteFromEnsembleDTO.getVideoId());
        addToEnsembleMapper.delete(wrapper);
        return Result.data(true);
    }
}
