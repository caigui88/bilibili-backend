package com.bilibili.video.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.bilibili.api.client.SearchClient;
import com.bilibili.common.domain.entity.video.audience_reactions.Collect;
import com.bilibili.common.domain.entity.video.audience_reactions.Like;
import com.bilibili.common.domain.entity.video.audience_reactions.Play;
import com.bilibili.common.domain.entity.video.video_production.Video;
import com.bilibili.common.domain.entity.video.video_production.VideoData;
import com.bilibili.common.domain.pojo.RecommendVideo;
import com.bilibili.common.mapper.video.audience_reactions.CollectMapper;
import com.bilibili.common.mapper.video.audience_reactions.LikeMapper;
import com.bilibili.common.mapper.video.audience_reactions.PlayMapper;
import com.bilibili.common.mapper.video.video_production.VideoMapper;
import com.bilibili.common.util.Result;
import com.bilibili.video.domain.dto.DeleteHistoryVideoDTO;
import com.bilibili.video.domain.vo.CommendVideoVO;
import com.bilibili.video.domain.vo.DetailVideoVO;
import com.bilibili.video.domain.vo.FirstPageVideoVO;
import com.bilibili.video.domain.vo.HistoryVideoVO;
import com.bilibili.video.mapper.VideoServiceMapper;
import com.bilibili.video.service.PlayService;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.bilibili.video.constant.Constant.HAS_PLAY;

/**
 *播放相关
 */
@Service
@Slf4j
public class PlayServiceImpl implements PlayService {
    @Resource
    PlayMapper playMapper;
    @Resource
    VideoMapper videoMapper;
    @Resource
    CollectMapper collectMapper;
    @Resource
    SearchClient searchClient;
    @Resource
    LikeMapper likeMapper;
    @Resource
    RedisTemplate objectRedisTemplate;
    @Resource
    VideoServiceMapper videoServiceMapper;
    /**
     *新增播放记录
     */
    @Override
    public Result<Boolean> addPlayRecord(int videoId, int userId){
        LambdaQueryWrapper<Play> wrapper=new LambdaQueryWrapper<>();
        wrapper.eq(Play::getVideoId,videoId);
        wrapper.eq(Play::getUserId,userId);
        //如果已经增加过了则不新增播放记录
        if(playMapper.selectList(wrapper).size()>0){
            return Result.success(HAS_PLAY);
        }else {
            playMapper.insert(new Play().setVideoId(videoId).setUserId(userId));
            return Result.success(true);
        }
    }
    /**
     *删除历史视频
     */
    @Override
    public Result<Boolean> deleteHistoryVideo(DeleteHistoryVideoDTO deleteHistoryVideoDTO){
        LambdaQueryWrapper<Play> wrapper=new LambdaQueryWrapper<>();
        wrapper.eq(Play::getUserId, deleteHistoryVideoDTO.getUserId());
        wrapper.eq(Play::getVideoId, deleteHistoryVideoDTO.getVideoId());
        playMapper.delete(wrapper);
        return Result.success(true);
    }
    /**
     *获取视频相关信息
     */
    @Override
    public Result<DetailVideoVO> getDetailVideo(Integer videoId, Integer userId, String collectGroupId) {
        //前端将收藏夹id用，拼接起来从而不用传集合就可以传递收藏夹集合，所以后端需要拆解成集合
        List<Integer> collectGroupIds = Arrays.stream(collectGroupId.split(","))
                .map(Integer::parseInt)
                .collect(Collectors.toList());
        MPJLambdaWrapper<Video> wrapper = new MPJLambdaWrapper<>();
        wrapper.select(Video::getName, Video::getIntro, Video::getCreateTime, Video::getId,Video::getUrl);
        wrapper.select(VideoData::getDanmakuCount, VideoData::getPlayCount, VideoData::getLikeCount, VideoData::getCollectCount);
        wrapper.leftJoin(VideoData.class, VideoData::getVideoId, Video::getId);
        wrapper.eq(Video::getId, videoId);
        LambdaQueryWrapper<Like> likeLambdaQueryWrapper=new LambdaQueryWrapper<>();
        LambdaQueryWrapper<Collect> collectLambdaQueryWrapper=new LambdaQueryWrapper<>();
        collectLambdaQueryWrapper.in(Collect::getCollectGroupId,collectGroupIds);
        collectLambdaQueryWrapper.eq(Collect::getVideoId,videoId);
        likeLambdaQueryWrapper.eq(Like::getVideoId,videoId);
        likeLambdaQueryWrapper.eq(Like::getUserId,userId);
        //获取视频相关信息和该视频自己是否已点赞与已收藏
        DetailVideoVO response = videoMapper.selectJoinOne(DetailVideoVO.class, wrapper);
        Like videoLike = likeMapper.selectOne(likeLambdaQueryWrapper);
        List<Collect> collects=collectMapper.selectList(collectLambdaQueryWrapper);
        if (videoLike != null) {
            response.setIsLiked(true);
        }
        if(collects.size()>0){
            response.setIsCollected(true);
        }
        return Result.data(response);
    }
    /**
     *获取推荐视频，远程调用es的推荐视频查询接口
     */
    @Override
    public Result<List<CommendVideoVO>> getRecommendVideo(String videoId) {
        List<RecommendVideo> list=searchClient.getRecommendVideo(videoId);
        List<CommendVideoVO> list1=new ArrayList<>();
        for(RecommendVideo recommendVideo : list){
            list1.add(new CommendVideoVO(recommendVideo));
        }
        return Result.data(list1);

    }
    /**
     *获取首页视频
     */
    @Override
    public Result<List<FirstPageVideoVO>> getFirstPageVideoResponse(Integer count) {
        //获取首页视频，并每次跳过前面十条防止刷出重复视频
        List<FirstPageVideoVO> responses=videoServiceMapper.getFirstPageVideo(count-1);
        return Result.data(responses);
    }
    /**
     *获取历史视频
     */
    @Override
    public Result<List<HistoryVideoVO>> getHistoryVideo(int userId) {
        return Result.data(videoServiceMapper.getHistoryVideo(userId));
    }
}
